package com.api.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.AuthorResponse;
import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.LibraryRepository;
import com.api.library.service.LibraryService;

@RestController
public class LibraryController{

    @Autowired
    LibraryRepository repository;

    @Autowired
    LibraryService service;

    @Autowired
    AuthorRepository authorRepository;

    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @PostMapping("/book") 
    public ResponseEntity<BookResponse> addBook(@RequestBody BookRequest bookRequest){
        HttpHeaders headers = new HttpHeaders();
        BookResponse response = new BookResponse();
        Book existingBook = repository.findByIsbn(bookRequest.getIsbn());
        if(existingBook == null){
            logger.info("Book does not exists so creating one");
            Book book = new Book();
            book.setIsbn(bookRequest.getIsbn());
            book.setBook_name(bookRequest.getBook_name());
            book.setAisle(bookRequest.getAisle());
            Author author = authorRepository.findById(bookRequest.getAuthorId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));;
            book.setAuthor(author);
            Book savedBook = repository.save(book);
            response.setId(savedBook.getId());
            response.setIsbn(savedBook.getIsbn());
            response.setAisle(savedBook.getAisle());
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(author.getId());
            authorResponse.setName(author.getAuthor_name());
            response.setAuthor(authorResponse);
            response.setMessage("Book successfully added");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }else{
            response.setAisle(existingBook.getAisle());
            response.setIsbn(existingBook.getIsbn());
            response.setId(existingBook.getId());
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(existingBook.getAuthor().getId());
            authorResponse.setName(existingBook.getAuthor().getAuthor_name());
            response.setAuthor(authorResponse);
            response.setMessage("Book already exists");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping(path = "/book")
    public ResponseEntity<List<Book>> getBooks(){
        List<Book> books = repository.findAll();
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(value = "id")Integer id){
        try{
            Book book = repository.findById(id).get();
            return new ResponseEntity<>(book, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/book", params = "authorName")
    public ResponseEntity<List<Book>> getBookByAuthor(@RequestParam(value = "authorName")String authorName){
        try{
           List<Book> books = repository.findByAuthorName(authorName); 
           return new ResponseEntity<>(books, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable(value = "id")Integer id, @RequestBody BookRequest bookRequest){
        try{
            Book book_res = repository.findById(id).get();
            book_res.setAisle(bookRequest.getAisle());
            book_res.setIsbn(bookRequest.getIsbn());
            book_res.setBook_name(bookRequest.getBook_name());
            Author author = authorRepository.findById(bookRequest.getAuthorId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));;
            book_res.setAuthor(author);
            repository.save(book_res);
            return new ResponseEntity<>(book_res, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<BookResponse> deleteBook(@PathVariable(value = "id")Integer id){
        try{
            Book book = repository.findById(id).get();
            repository.delete(book);
            BookResponse response = new BookResponse();
            response.setMessage("Book deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    
}
