package com.codestorykh.knowledge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeResponse {

    private Long id;
    private String title;
    private String content;
    private String category;
    private List<String> tags;

    @JsonProperty("schema_type")
    private String schemaType;

    @JsonProperty("confidence_score")
    private Double confidenceScore;

    @JsonProperty("source_url")
    private String sourceUrl;

    private String author;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("@context")
    private String context;

    @JsonProperty("@type")
    private String type;
}
