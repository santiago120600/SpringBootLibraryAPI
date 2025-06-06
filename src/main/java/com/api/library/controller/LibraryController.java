package com.api.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.library.repository.LibraryRepository;
import com.api.library.service.LibraryService;

@RestController
public class LibraryController{

    @Autowired
    LibraryRepository repository;

    @Autowired
    AddBookResponse add; 

    @Autowired
    LibraryService service;

    @PostMapping("/book") 
    public ResponseEntity addBook(@RequestBody Library library){
        String id = service.buildId(library.getIsbn(), library.getAisle());
        HttpHeaders headers = new HttpHeaders();
        headers.add("environment","QA");
        add.setId(id);
        if(!service.checkBookAlreadyExists(id)){
            library.setId(id);
            repository.save(library);
            add.setMessage("Book successfully added");
            return new ResponseEntity<AddBookResponse>(add, headers,HttpStatus.CREATED);
        }else{
            add.setMessage("Book already exists");
            return new ResponseEntity<AddBookResponse>(add, headers,HttpStatus.ACCEPTED);
        }
    }
}
