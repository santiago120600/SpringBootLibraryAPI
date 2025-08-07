package com.api.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BookRequest{
    private String title;
    private String isbn;
    @JsonProperty("aisle_number")
    private int aisleNumber;
    @JsonProperty("author_id")
    private Integer authorId;
}
