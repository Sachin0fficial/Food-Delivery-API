package com.codestorykh.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final Counter ordersCreatedCounter;
    private final Counter ordersFulfilledCounter;
    private final Counter aiQueriesCounter;
    private final Counter knowledgeBaseUpdatesCounter;
    private final Timer aiResponseTimer;

    public MetricsService(MeterRegistry registry) {
        this.ordersCreatedCounter = Counter.builder("business.orders.created")
                .description("Total orders created")
                .register(registry);
        this.ordersFulfilledCounter = Counter.builder("business.orders.fulfilled")
                .description("Total orders delivered")
                .register(registry);
        this.aiQueriesCounter = Counter.builder("ai.queries.total")
                .description("Total AI chatbot queries")
                .register(registry);
        this.knowledgeBaseUpdatesCounter = Counter.builder("knowledge.base.updates")
                .description("Total knowledge base updates")
                .register(registry);
        this.aiResponseTimer = Timer.builder("ai.response.duration")
                .description("AI response time")
                .register(registry);
    }

    public void incrementOrdersCreated() {
        ordersCreatedCounter.increment();
    }

    public void incrementOrdersFulfilled() {
        ordersFulfilledCounter.increment();
    }

    public void incrementAiQueries() {
        aiQueriesCounter.increment();
    }

    public void incrementKnowledgeBaseUpdates() {
        knowledgeBaseUpdatesCounter.increment();
    }

    public Timer getAiResponseTimer() {
        return aiResponseTimer;
    }
}
