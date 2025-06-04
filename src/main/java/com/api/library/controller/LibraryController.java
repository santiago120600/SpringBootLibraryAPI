package com.api.library.controller;

import java.net.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.library.repository.LibraryRepository;

@RestController
public class LibraryController{

    @Autowired
    LibraryRepository repository;

    @Autowired
    AddBookResponse add; 

    @PostMapping("/book") 
    public ResponseEntity addBook(@RequestBody Library library){
        library.setId(library.getIsbn()+library.getAisle());
        repository.save(library);
        HttpHeaders headers = new HttpHeaders();
        headers.add("environment","QA");
        add.setMessage("Book successfully addded");
        library.setId(library.getIsbn()+library.getAisle());
        add.setMessage("Book successfully added");
        add.setId(library.getIsbn()+library.getAisle());
        return new ResponseEntity<AddBookResponse>(add, headers,HttpStatus.CREATED);
    }
}
