package com.api.library.dto;

import lombok.Data;

@Data
public class Pagination {
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
