package com.api.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Pagination {
    @JsonProperty("current_page")
    private int currentPage;
    @JsonProperty("per_page")
    private int pageSize;
    @JsonProperty("total")
    private long totalElements;
    @JsonProperty("total_pages")
    private int totalPages;
}
