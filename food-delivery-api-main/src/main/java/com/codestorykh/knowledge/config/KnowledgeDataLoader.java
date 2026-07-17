package com.codestorykh.knowledge.config;

import com.codestorykh.knowledge.model.KnowledgeEntry;
import com.codestorykh.knowledge.repository.KnowledgeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class KnowledgeDataLoader implements CommandLineRunner {

    private final KnowledgeRepository knowledgeRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) {
        if (knowledgeRepository.count() > 0) {
            log.info("Knowledge base already contains {} entries, skipping initial load", knowledgeRepository.count());
            return;
        }

        try {
            ClassPathResource resource = new ClassPathResource("data/initial-knowledge.json");
            InputStream inputStream = resource.getInputStream();

            List<KnowledgeEntryData> entries = objectMapper.readValue(
                inputStream, new TypeReference<List<KnowledgeEntryData>>() {}
            );

            List<KnowledgeEntry> knowledgeEntries = entries.stream()
                .map(this::toEntity)
                .toList();

            knowledgeRepository.saveAll(knowledgeEntries);
            log.info("Loaded {} initial knowledge base entries", knowledgeEntries.size());

        } catch (Exception e) {
            log.warn("Could not load initial knowledge base data: {}", e.getMessage());
        }
    }

    private KnowledgeEntry toEntity(KnowledgeEntryData data) {
        return KnowledgeEntry.builder()
            .title(data.title())
            .content(data.content())
            .category(data.category())
            .tags(data.tags())
            .schemaType(data.schemaType() != null ? data.schemaType() : "Article")
            .confidenceScore(data.confidenceScore() != null ? data.confidenceScore() : 1.0)
            .sourceUrl(data.sourceUrl())
            .author(data.author() != null ? data.author() : "system")
            .build();
    }

    private record KnowledgeEntryData(
        String title,
        String content,
        String category,
        List<String> tags,
        String schemaType,
        Double confidenceScore,
        String sourceUrl,
        String author
    ) {}
}
