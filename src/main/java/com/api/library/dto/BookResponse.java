package com.api.library.dto;

import lombok.Data;

@Data
public class BookResponse{
    private Integer id;
    private String message;
    private String isbn;
    private Integer aisle;
    private AuthorResponse author;
}

