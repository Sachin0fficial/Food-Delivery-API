package com.codestorykh.knowledge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 10000)
    private String content;

    @NotBlank(message = "Category is required")
    private String category;

    private List<String> tags;

    @JsonProperty("schema_type")
    private String schemaType;

    @JsonProperty("confidence_score")
    private Double confidenceScore;

    @JsonProperty("source_url")
    private String sourceUrl;

    private String author;
}
