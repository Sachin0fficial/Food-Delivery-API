package com.codestorykh.ai.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.gemini")
public class GeminiConfig {
    private String apiKey;
    private String model = "gemini-2.0-flash";
    private double temperature = 0.7;
    private int maxOutputTokens = 1024;
    private int timeoutSeconds = 30;
}
