package com.api.library.dto;

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
}
