package com.api.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse{
    private Integer id;
    private String title;
    private String isbn;
    @JsonProperty("aisle_number")
    private Integer aisleNumber;
    private AuthorResponse author;
}

