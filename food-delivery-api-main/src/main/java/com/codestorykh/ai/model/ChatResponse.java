package com.codestorykh.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String answer;

    @JsonProperty("conversation_id")
    private String conversationId;

    @JsonProperty("source_references")
    private List<String> sourceReferences;

    private double confidence;

    @JsonProperty("response_time_ms")
    private long responseTimeMs;

    private Instant timestamp;
}
