package com.api.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookRequest{
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "ISBN is required")
    @Size(max = 20, message = "ISBN cannot exceed 20 characters")
    private String isbn;

    @NotNull(message = "Aisle number is required")
    @JsonProperty("aisle_number")
    private Integer aisleNumber;

    @NotNull(message = "Author ID is required")
    @JsonProperty("author_id")
    private Integer authorId;
}
