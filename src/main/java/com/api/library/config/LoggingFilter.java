package com.api.library.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Autowired
    private Environment environment;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("method", wrappedRequest.getMethod());
        MDC.put("uri", wrappedRequest.getRequestURI());
        MDC.put("userAgent", wrappedRequest.getHeader("User-Agent"));
        MDC.put("clientIp", getClientIpAddress(wrappedRequest));
        MDC.put("protocol", wrappedRequest.getProtocol());
        MDC.put("headers", Collections.list(wrappedRequest.getHeaderNames()).toString());
        MDC.put("parameters", Collections.list(wrappedRequest.getParameterNames()).toString());

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
            String requestBody = getRequestBody(wrappedRequest);
            MDC.put("requestHeaders", getHeaders(httpRequest));
            MDC.put("requestBody", requestBody);

            if (!isProdProfile() || isErrorStatus(httpResponse.getStatus())) {
                logRequest(wrappedRequest);
            }

            MDC.remove("requestBody");
            MDC.remove("requestHeaders");
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            MDC.put("processingTime", String.valueOf(processingTime));
            MDC.put("status", String.valueOf(httpResponse.getStatus()));
            String responseBody = getResponseBody(wrappedResponse);
            MDC.put("responseBody", responseBody);

            // Copy body to response first
            wrappedResponse.copyBodyToResponse();

            MDC.put("responseHeaders", getHeaders(httpResponse));

            if (!isProdProfile() || isErrorStatus(httpResponse.getStatus())) {
                logResponse(httpResponse, processingTime);
            }

            MDC.clear();
        }
    }

    private boolean isProdProfile() {
        return environment.getActiveProfiles().length > 0 &&
               java.util.Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }

    private boolean isErrorStatus(int status) {
        return status >= 400; // 4xx (client errors) and 5xx (server errors)
    }

    private void logRequest(HttpServletRequest request) {
        try {
            logger.info("Request received", Collections.singletonMap("type", "request"));
        } catch (Exception e) {
            logger.warn("Failed to log request", e);
        }
    }

    private void logResponse(HttpServletResponse response, long processingTime) {
        try {
            logger.info("Response sent",
                    Collections.singletonMap("type", "response"));
        } catch (Exception e) {
            logger.warn("Failed to log response", e);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        try {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                // Limit the body size to prevent logging huge payloads
                int maxLength = 1024; // Adjust as needed (e.g., 1KB limit)
                String body = new String(content, StandardCharsets.UTF_8);
                return body.length() > maxLength ? body.substring(0, maxLength) + "..." : body;
            }
            return "";
        } catch (Exception e) {
            logger.warn("Failed to read request body", e);
            return "[Error reading body]";
        }
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        try {
            byte[] content = response.getContentAsByteArray();
            if (content.length > 0) {
                int maxLength = 1024; // Adjust as needed (e.g., 1KB limit)
                String body = new String(content, StandardCharsets.UTF_8);
                return body.length() > maxLength ? body.substring(0, maxLength) + "..." : body;
            }
        } catch (Exception e) {
            logger.warn("Error reading response payload", e);
            return "[Error reading body]";
        }
        return "";
    }

    private String getHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(
                        header -> header,
                        request::getHeader,
                        (v1, v2) -> v1,
                        () -> new java.util.LinkedHashMap<>()))
                .toString();
    }

    private String getHeaders(HttpServletResponse response) {
        return response.getHeaderNames().stream()
                .collect(Collectors.toMap(
                        header -> header,
                        response::getHeader,
                        (v1, v2) -> v1,
                        () -> new java.util.LinkedHashMap<>()))
                .toString();
    }

}
