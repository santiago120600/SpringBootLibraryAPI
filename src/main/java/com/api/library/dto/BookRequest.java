package com.api.library.dto;

import lombok.Data;

@Data
public class BookRequest{
    private String book_name;
    private String isbn;
    private int aisle;
    private Integer authorId;
}
