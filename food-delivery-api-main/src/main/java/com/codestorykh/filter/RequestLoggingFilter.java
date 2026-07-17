package com.codestorykh.filter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String MDC_CORRELATION_ID = "correlationId";
    private static final String MDC_ENDPOINT = "endpoint";

    private final Counter requestCounter;
    private final Timer requestLatencyTimer;

    public RequestLoggingFilter(MeterRegistry registry) {
        this.requestCounter = Counter.builder("http.server.requests.count")
                .description("Total HTTP requests received")
                .register(registry);
        this.requestLatencyTimer = Timer.builder("http.server.requests.latency")
                .description("HTTP request latency")
                .register(registry);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(MDC_CORRELATION_ID, correlationId);
        MDC.put(MDC_ENDPOINT, request.getRequestURI());
        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.info("Incoming request: method={}, uri={}", method, uri);

        long startTime = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationNanos = System.nanoTime() - startTime;
            Duration duration = Duration.ofNanos(durationNanos);
            int status = response.getStatus();

            requestCounter.increment();
            requestLatencyTimer.record(duration);

            log.info("Completed request: method={}, uri={}, status={}, durationMs={}",
                    method, uri, status, duration.toMillis());

            MDC.remove(MDC_CORRELATION_ID);
            MDC.remove(MDC_ENDPOINT);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator");
    }
}
