package com.codestorykh.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        long startTime = System.currentTimeMillis();
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            long responseTime = System.currentTimeMillis() - startTime;
            return Health.up()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("responseTimeMs", responseTime)
                    .withDetail("status", "Connection verified")
                    .build();
        } catch (Exception ex) {
            long responseTime = System.currentTimeMillis() - startTime;
            return Health.down()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("responseTimeMs", responseTime)
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }
}
