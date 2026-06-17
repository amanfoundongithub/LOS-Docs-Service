package com.loan_org.document_service.infrastructure.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@Slf4j

public class MdcInterceptor implements HandlerInterceptor {

    /**
     * Correlation configurations
     */
    @Value("${filter.mdc.correlation.header}")
    private String correlationHeader;

    @Value("${filter.mdc.correlation.key}")
    private String mdcCorrelationKey;

    /**
     * Trace configurations
     */
    @Value("${filter.mdc.trace.header}")
    private String traceHeader;

    @Value("${filter.mdc.trace.key}")
    private String mdcTraceKey;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        // Handle correlationId first
        String correlationId = request.getHeader(correlationHeader);
        if(correlationId == null || correlationId.isBlank()) {
            correlationId = "CORR-LOS-" + UUID.randomUUID();
            log.warn("Missing tracking header [{}]. Generated fallback correlationId: {}",
                    correlationHeader,
                    correlationId);
        }

        // Handle traceId next
        String traceId = request.getHeader(traceHeader);
        if(traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
            log.warn("Missing tracking header [{}]. Generated standalone traceId: {}",
                    traceHeader,
                    traceId);
        }

        // Add them to MDC
        MDC.put(mdcTraceKey, traceId);
        MDC.put(mdcCorrelationKey, correlationId);

        // Add them to response header
        response.addHeader(correlationHeader, correlationId);
        response.addHeader(traceHeader, traceId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        MDC.clear();
    }


}
