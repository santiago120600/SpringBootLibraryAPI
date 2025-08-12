package com.api.library.dto;

import lombok.Data;

@Data
public class ResponseWrapper {
    private String message;
    private String status;
    private Object data;
}
