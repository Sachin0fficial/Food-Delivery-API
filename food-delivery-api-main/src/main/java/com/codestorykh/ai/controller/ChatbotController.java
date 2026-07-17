package com.codestorykh.ai.controller;

import com.codestorykh.ai.model.ChatRequest;
import com.codestorykh.ai.model.ChatResponse;
import com.codestorykh.ai.service.GeminiService;
import com.codestorykh.exception.ApiResponseUtil;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/ai")
@RequiredArgsConstructor
@Slf4j
public class ChatbotController {

    private final GeminiService geminiService;

    @PostMapping("/chat")
    @Timed(value = "api.ai.chat", description = "AI chat endpoint response time")
    public ResponseEntity<Object> chat(@Valid @RequestBody ChatRequest request) {
        log.info("AI chat request received, conversationId: {}", request.getConversationId());
        ChatResponse response = geminiService.chat(request);
        return ResponseEntity.ok(ApiResponseUtil.successResponse(response));
    }

    @GetMapping("/health")
    public ResponseEntity<Object> aiHealth() {
        return ResponseEntity.ok(Map.of(
            "status", "operational",
            "model", "gemini-2.0-flash",
            "capabilities", List.of("chat", "knowledge-query", "api-help")
        ));
    }
}
