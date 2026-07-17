package com.codestorykh.knowledge.dto;

import lombok.Data;

import java.util.List;

@Data
public class KnowledgeSearchRequest {
    private String query;
    private String category;
    private List<String> tags;
    private Integer limit;
}
