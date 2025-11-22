package com.api.library.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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
@Order(1)
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Autowired
    private Environment environment;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Only wrap if not already wrapped
        ContentCachingRequestWrapper wrappedRequest = 
            httpRequest instanceof ContentCachingRequestWrapper ? 
            (ContentCachingRequestWrapper) httpRequest : 
            new ContentCachingRequestWrapper(httpRequest);
            
        ContentCachingResponseWrapper wrappedResponse = 
            httpResponse instanceof ContentCachingResponseWrapper ? 
            (ContentCachingResponseWrapper) httpResponse : 
            new ContentCachingResponseWrapper(httpResponse);

        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("method", wrappedRequest.getMethod());
        MDC.put("uri", wrappedRequest.getRequestURI());

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            MDC.put("processingTime", String.valueOf(processingTime));
            MDC.put("status", String.valueOf(wrappedResponse.getStatus()));

            // Log based on profile and status
            if (!isProdProfile() || isErrorStatus(wrappedResponse.getStatus())) {
                logRequestResponse(wrappedRequest, wrappedResponse, processingTime);
            }

            // Copy body to response
            wrappedResponse.copyBodyToResponse();
            MDC.clear();
        }
    }

    private boolean isProdProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles.length > 0 && 
               java.util.Arrays.asList(activeProfiles).contains("prod");
    }

    private boolean isErrorStatus(int status) {
        return status >= 400;
    }

    private void logRequestResponse(ContentCachingRequestWrapper request, 
                                  ContentCachingResponseWrapper response, 
                                  long processingTime) {
        try {
            String requestBody = getRequestBody(request);
            String responseBody = getResponseBody(response);
            
            logger.info("Request: {} {}, Response: {} {}, Time: {}ms", 
                request.getMethod(), request.getRequestURI(),
                response.getStatus(), responseBody, processingTime);
                
        } catch (Exception e) {
            logger.warn("Failed to log request/response", e);
        }
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        try {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                String body = new String(content, StandardCharsets.UTF_8);
                return body.length() > 1024 ? body.substring(0, 1024) + "..." : body;
            }
        } catch (Exception e) {
            logger.warn("Failed to read request body", e);
        }
        return "";
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        try {
            byte[] content = response.getContentAsByteArray();
            if (content.length > 0) {
                String body = new String(content, StandardCharsets.UTF_8);
                return body.length() > 1024 ? body.substring(0, 1024) + "..." : body;
            }
        } catch (Exception e) {
            logger.warn("Error reading response payload", e);
        }
        return "";
    }
}