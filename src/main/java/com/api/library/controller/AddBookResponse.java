package com.api.library.controller;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class AddBookResponse{
    private String message;
    private String id;

}
