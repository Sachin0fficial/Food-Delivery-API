package com.codestorykh.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AIServiceHealthIndicator implements HealthIndicator {

    private final WebClient webClient;

    @Value("${ai.gemini.api-key:}")
    private String apiKey;

    public AIServiceHealthIndicator(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();
    }

    @Override
    public Health health() {
        if (apiKey == null || apiKey.isBlank()) {
            return Health.down()
                    .withDetail("service", "Gemini AI")
                    .withDetail("error", "API key not configured")
                    .build();
        }

        long startTime = System.currentTimeMillis();
        try {
            webClient.get()
                .uri("/v1beta/models")
                .header("x-goog-api-key", apiKey)
                .retrieve()
                .toBodilessEntity()
                .block();

            long responseTime = System.currentTimeMillis() - startTime;
            return Health.up()
                    .withDetail("service", "Gemini AI")
                    .withDetail("responseTimeMs", responseTime)
                    .withDetail("model", "gemini-2.0-flash")
                    .build();
        } catch (Exception ex) {
            long responseTime = System.currentTimeMillis() - startTime;
            return Health.down()
                    .withDetail("service", "Gemini AI")
                    .withDetail("responseTimeMs", responseTime)
                    .withDetail("error", "Service unreachable or invalid API key")
                    .build();
        }
    }
}
