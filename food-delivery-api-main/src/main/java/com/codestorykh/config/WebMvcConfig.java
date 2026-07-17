package com.codestorykh.config;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MeterRegistry meterRegistry;

    public WebMvcConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiMetricsInterceptor(meterRegistry))
                .addPathPatterns("/api/**");
    }

    private static class ApiMetricsInterceptor implements HandlerInterceptor {

        private static final Logger log = LoggerFactory.getLogger(ApiMetricsInterceptor.class);
        private static final String START_TIME_ATTR = "apiMetrics.startTime";
        private final MeterRegistry meterRegistry;

        ApiMetricsInterceptor(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
        }

        @Override
        public boolean preHandle(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler) {
            request.setAttribute(START_TIME_ATTR, System.nanoTime());
            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object handler,
                                    Exception ex) {
            Long startTime = (Long) request.getAttribute(START_TIME_ATTR);
            if (startTime == null) {
                return;
            }

            long durationNanos = System.nanoTime() - startTime;
            String uri = request.getRequestURI();
            String method = request.getMethod();
            int status = response.getStatus();

            meterRegistry.timer("api.endpoint.duration",
                    "uri", uri,
                    "method", method,
                    "status", String.valueOf(status)
            ).record(java.time.Duration.ofNanos(durationNanos));

            meterRegistry.counter("api.endpoint.calls",
                    "uri", uri,
                    "method", method,
                    "status", String.valueOf(status)
            ).increment();

            if (ex != null) {
                log.warn("Request to {} {} completed with exception: {}",
                        method, uri, ex.getMessage());
            }
        }
    }
}
