package com.codestorykh.knowledge.service;

import com.codestorykh.knowledge.dto.KnowledgeRequest;
import com.codestorykh.knowledge.dto.KnowledgeResponse;
import com.codestorykh.knowledge.model.KnowledgeEntry;
import com.codestorykh.knowledge.repository.KnowledgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KnowledgeBaseService {

    private final KnowledgeRepository knowledgeRepository;

    @Cacheable(value = "knowledge-search", key = "#query")
    public List<KnowledgeEntry> search(String query) {
        log.info("Searching knowledge base for: {}", query);
        return knowledgeRepository.searchByQuery(query);
    }

    public List<KnowledgeResponse> getAll() {
        return knowledgeRepository.findByIsActiveTrue().stream()
            .map(this::toResponse)
            .toList();
    }

    public KnowledgeResponse getById(Long id) {
        KnowledgeEntry entry = knowledgeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Knowledge entry not found with id: " + id));
        return toResponse(entry);
    }

    public List<KnowledgeResponse> getByCategory(String category) {
        return knowledgeRepository.findByCategoryAndIsActiveTrue(category).stream()
            .map(this::toResponse)
            .toList();
    }

    public List<String> getAllCategories() {
        return knowledgeRepository.findAllCategories();
    }

    @Transactional
    @CacheEvict(value = "knowledge-search", allEntries = true)
    public KnowledgeResponse create(KnowledgeRequest request) {
        log.info("Creating knowledge entry: {}", request.getTitle());
        KnowledgeEntry entry = KnowledgeEntry.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .category(request.getCategory())
            .tags(request.getTags())
            .schemaType(request.getSchemaType() != null ? request.getSchemaType() : "Article")
            .confidenceScore(request.getConfidenceScore() != null ? request.getConfidenceScore() : 1.0)
            .sourceUrl(request.getSourceUrl())
            .author(request.getAuthor())
            .build();

        KnowledgeEntry saved = knowledgeRepository.save(entry);
        return toResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "knowledge-search", allEntries = true)
    public KnowledgeResponse update(Long id, KnowledgeRequest request) {
        KnowledgeEntry entry = knowledgeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Knowledge entry not found with id: " + id));

        entry.setTitle(request.getTitle());
        entry.setContent(request.getContent());
        entry.setCategory(request.getCategory());
        entry.setTags(request.getTags());
        if (request.getSchemaType() != null) entry.setSchemaType(request.getSchemaType());
        if (request.getConfidenceScore() != null) entry.setConfidenceScore(request.getConfidenceScore());
        entry.setSourceUrl(request.getSourceUrl());
        entry.setAuthor(request.getAuthor());

        KnowledgeEntry saved = knowledgeRepository.save(entry);
        return toResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "knowledge-search", allEntries = true)
    public void delete(Long id) {
        KnowledgeEntry entry = knowledgeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Knowledge entry not found with id: " + id));
        entry.setIsActive(false);
        knowledgeRepository.save(entry);
    }

    @Cacheable(value = "knowledge-export")
    public List<KnowledgeResponse> exportAsOKF() {
        return knowledgeRepository.findByIsActiveTrue().stream()
            .map(this::toOKFResponse)
            .toList();
    }

    private KnowledgeResponse toResponse(KnowledgeEntry entry) {
        return KnowledgeResponse.builder()
            .id(entry.getId())
            .title(entry.getTitle())
            .content(entry.getContent())
            .category(entry.getCategory())
            .tags(entry.getTags())
            .schemaType(entry.getSchemaType())
            .confidenceScore(entry.getConfidenceScore())
            .sourceUrl(entry.getSourceUrl())
            .author(entry.getAuthor())
            .createdAt(entry.getCreatedAt())
            .updatedAt(entry.getUpdatedAt())
            .isActive(entry.getIsActive())
            .build();
    }

    private KnowledgeResponse toOKFResponse(KnowledgeEntry entry) {
        KnowledgeResponse response = toResponse(entry);
        response.setContext("https://schema.org");
        response.setType(entry.getSchemaType() != null ? entry.getSchemaType() : "Article");
        return response;
    }
}
