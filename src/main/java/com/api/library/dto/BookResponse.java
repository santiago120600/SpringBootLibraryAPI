package com.api.library.dto;

import lombok.Data;

@Data
public class BookResponse{
    private Integer id;
    private String message;
    private String title;
    private String isbn;
    private Integer aisleNumber;
    private AuthorResponse author;
}

