package com.codestorykh.ai.service;

import com.codestorykh.ai.model.ChatRequest;
import com.codestorykh.ai.model.ChatResponse;
import com.codestorykh.knowledge.model.KnowledgeEntry;
import com.codestorykh.knowledge.service.KnowledgeBaseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class GeminiService {

    private final WebClient webClient;
    private final KnowledgeBaseService knowledgeBaseService;
    private final ObjectMapper objectMapper;

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.model:gemini-2.0-flash}")
    private String model;

    public GeminiService(WebClient.Builder webClientBuilder,
                         KnowledgeBaseService knowledgeBaseService,
                         ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
            .baseUrl("https://generativelanguage.googleapis.com/v1beta")
            .build();
        this.knowledgeBaseService = knowledgeBaseService;
        this.objectMapper = objectMapper;
    }

    public ChatResponse chat(ChatRequest request) {
        long startTime = System.currentTimeMillis();

        String context = "";
        List<String> sources = List.of();

        if (request.isIncludeContext()) {
            List<KnowledgeEntry> knowledgeResults = knowledgeBaseService.search(request.getMessage());
            context = knowledgeResults.stream()
                .map(KnowledgeEntry::getContent)
                .reduce("", (a, b) -> a + "\n" + b);
            sources = knowledgeResults.stream()
                .map(entry -> entry.getCategory() + "/" + entry.getTitle())
                .toList();
        }

        String systemPrompt = buildSystemPrompt(context);
        String fullPrompt = systemPrompt + "\n\nUser Question: " + request.getMessage();

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(Map.of("text", fullPrompt)))
            ),
            "generationConfig", Map.of(
                "temperature", 0.7,
                "maxOutputTokens", 1024,
                "topP", 0.9
            )
        );

        try {
            String response = webClient.post()
                .uri("/models/{model}:generateContent?key={key}", model, apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            String answer = extractAnswer(response);
            long duration = System.currentTimeMillis() - startTime;

            return ChatResponse.builder()
                .answer(answer)
                .conversationId(request.getConversationId() != null
                    ? request.getConversationId() : UUID.randomUUID().toString())
                .sourceReferences(sources)
                .confidence(sources.isEmpty() ? 0.6 : 0.9)
                .responseTimeMs(duration)
                .timestamp(Instant.now())
                .build();

        } catch (Exception e) {
            log.error("Gemini API call failed: {}", e.getMessage());
            long duration = System.currentTimeMillis() - startTime;
            return ChatResponse.builder()
                .answer("I apologize, but I'm unable to process your request at this time. Please try again later.")
                .conversationId(request.getConversationId() != null
                    ? request.getConversationId() : UUID.randomUUID().toString())
                .sourceReferences(List.of())
                .confidence(0.0)
                .responseTimeMs(duration)
                .timestamp(Instant.now())
                .build();
        }
    }

    private String buildSystemPrompt(String context) {
        String contextBlock = context.isEmpty() ? "No specific context available." : context;
        return """
            You are an AI assistant for a Food Delivery platform. You have deep knowledge about:
            - Our food delivery services, APIs, and platform capabilities
            - Restaurant partners, delivery operations, and order management
            - Industry best practices in food delivery and logistics
            - Customer support, common issues, and resolutions
            - Technical API documentation and integration guides
            
            Use the following knowledge base context to provide accurate, helpful responses.
            If the context doesn't contain relevant information, use your general knowledge but \
            indicate that the answer is based on general knowledge rather than our specific documentation.
            
            Knowledge Base Context:
            """ + contextBlock;
    }

    private String extractAnswer(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            return root.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText("Unable to generate response.");
        } catch (Exception e) {
            log.error("Failed to parse Gemini response: {}", e.getMessage());
            return "Unable to process the response. Please try again.";
        }
    }
}
