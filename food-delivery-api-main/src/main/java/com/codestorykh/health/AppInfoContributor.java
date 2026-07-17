package com.codestorykh.health;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class AppInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("app", Map.of(
                "name", "Food Delivery API",
                "version", "1.0.0",
                "apiVersion", "v1",
                "buildTime", Instant.now().toString(),
                "javaVersion", System.getProperty("java.version"),
                "features", List.of(
                        "Order Management",
                        "Restaurant Management",
                        "Delivery Partner Tracking",
                        "AI-Powered Chatbot",
                        "Knowledge Base Integration",
                        "Payment Processing",
                        "Menu Item Management"
                )
        ));
    }
}
