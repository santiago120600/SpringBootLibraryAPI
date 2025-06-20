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

import com.api.library.model.Book;
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
    public ResponseEntity addBook(@RequestBody Book book){
        String id = service.buildId(book.getIsbn(), book.getAisle());
        HttpHeaders headers = new HttpHeaders();
        headers.add("environment","QA");
        add.setId(id);
        if(!service.checkBookAlreadyExists(id)){
            book.setId(id);
            repository.save(book);
            add.setMessage("Book successfully added");
            return new ResponseEntity<AddBookResponse>(add, headers,HttpStatus.CREATED);
        }else{
            add.setMessage("Book already exists");
            return new ResponseEntity<AddBookResponse>(add, headers,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping(path = "/book")
    public ResponseEntity getBooks(){
        List<Book> books = repository.findAll();
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public Book getBookById(@PathVariable(value = "id")String id){
        try{
            Book book = repository.findById(id).get();
            return book;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/book", params = "authorName")
    public ResponseEntity getBookByAuthor(@RequestParam(value = "authorName")String authorName){
        try{
           List<Book> books = repository.findByAuthor(authorName); 
           return new ResponseEntity<>(books, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/book/{id}")
    public ResponseEntity updateBook(@PathVariable(value = "id")String id, @RequestBody Book book_req){
        try{
            Book book_res = repository.findById(id).get();
            book_res.setAisle(book_req.getAisle());
            book_res.setIsbn(book_req.getIsbn());
            book_res.setAuthor(book_req.getAuthor());
            book_res.setBook_name(book_req.getBook_name());
            repository.save(book_res);
            return new ResponseEntity<>(book_res, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity deleteBook(@PathVariable(value = "id")String id){
        try{
            Book book = repository.findById(id).get();
            repository.delete(book);
            return new ResponseEntity("Book deleted successfully",HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    
}
