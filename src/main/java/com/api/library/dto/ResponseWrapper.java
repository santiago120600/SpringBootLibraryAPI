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
public class ResponseWrapper {
    private String message;
    private String status;
    private Object data;
    private Pagination pagination;

    @JsonProperty("trace_id")
    public String getTraceId() {
        return MDC.get("traceId");
    }
}
