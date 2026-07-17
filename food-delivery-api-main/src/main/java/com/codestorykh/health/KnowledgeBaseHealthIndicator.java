package com.codestorykh.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class KnowledgeBaseHealthIndicator implements HealthIndicator {

    @Value("${knowledge.base.path:knowledge-base}")
    private String knowledgeBasePath;

    @Override
    public Health health() {
        Path basePath = Paths.get(knowledgeBasePath);

        if (!Files.exists(basePath)) {
            return Health.down()
                    .withDetail("component", "Knowledge Base")
                    .withDetail("path", knowledgeBasePath)
                    .withDetail("error", "Knowledge base directory not found")
                    .build();
        }

        if (!Files.isReadable(basePath)) {
            return Health.down()
                    .withDetail("component", "Knowledge Base")
                    .withDetail("path", knowledgeBasePath)
                    .withDetail("error", "Knowledge base directory not readable")
                    .build();
        }

        try {
            long fileCount = Files.walk(basePath)
                    .filter(Files::isRegularFile)
                    .count();
            long totalSizeBytes = Files.walk(basePath)
                    .filter(Files::isRegularFile)
                    .mapToLong(path -> {
                        try {
                            return Files.size(path);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();

            return Health.up()
                    .withDetail("component", "Knowledge Base")
                    .withDetail("path", knowledgeBasePath)
                    .withDetail("fileCount", fileCount)
                    .withDetail("totalSizeBytes", totalSizeBytes)
                    .withDetail("accessible", true)
                    .build();
        } catch (IOException ex) {
            return Health.down()
                    .withDetail("component", "Knowledge Base")
                    .withDetail("path", knowledgeBasePath)
                    .withDetail("error", "Failed to scan knowledge base: " + ex.getMessage())
                    .build();
        }
    }
}
