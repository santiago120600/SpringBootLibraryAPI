package com.api.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping(path = "/book")
    public ResponseEntity getBooks(){
        List<Library> books = repository.findAll();
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public Library getBookById(@PathVariable(value = "id")String id){
        try{
            Library lib = repository.findById(id).get();
            return lib;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/book", params = "authorName")
    public ResponseEntity getBookByAuthor(@RequestParam(value = "authorName")String authorName){
        try{
           List<Library> books = repository.findByAuthor(authorName); 
           return new ResponseEntity<>(books, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/book/{id}")
    public ResponseEntity updateBook(@PathVariable(value = "id")String id, @RequestBody Library library){
        try{
            Library book = repository.findById(id).get();
            book.setAisle(library.getAisle());
            book.setIsbn(library.getIsbn());
            book.setAuthor(library.getAuthor());
            book.setBook_name(library.getBook_name());
            repository.save(book);
            return new ResponseEntity<>(book, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity deleteBook(@PathVariable(value = "id")String id){
        try{
            Library book = repository.findById(id).get();
            repository.delete(book);
            return new ResponseEntity(HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    
}
