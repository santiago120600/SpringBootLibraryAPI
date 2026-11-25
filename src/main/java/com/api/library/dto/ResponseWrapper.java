package com.api.library.dto;

import org.slf4j.MDC;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T> {
    private String message;
    private String status;
    private T data;
    private Pagination pagination;

    @JsonProperty("trace_id")
    public String getTraceId() {
        return MDC.get("traceId");
    }

    public static <T> ResponseWrapper<T> of(String message, String status, T data) {
        return ResponseWrapper.<T>builder()
                .message(message)
                .status(status)
                .data(data)
                .build();
    }

    public static <T> ResponseWrapper<T> of(String message, String status, T data, Pagination pagination) {
        return ResponseWrapper.<T>builder()
                .message(message)
                .status(status)
                .data(data)
                .pagination(pagination)
                .build();
    }
}
