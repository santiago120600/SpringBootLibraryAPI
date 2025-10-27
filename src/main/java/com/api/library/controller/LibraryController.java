package com.api.library.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.dto.Pagination;
import com.api.library.dto.ResponseWrapper;
import com.api.library.model.Author;
import com.api.library.service.AuthorService;
import com.api.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.api.library.model.Book;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Library API", description = "API endpoints for managing books and authors")
public class LibraryController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @PostMapping("/books")
    @Operation(summary = "Add a new book", description = "Creates a new book with the given details")
    public ResponseEntity<ResponseWrapper> addBook(@Valid @RequestBody BookRequest bookRequest) {
        Book book = new Book();
        book.setIsbn(bookRequest.getIsbn());
        book.setTitle(bookRequest.getTitle());
        book.setAisleNumber(bookRequest.getAisleNumber());
        
        Book savedBook = bookService.addBook(book, bookRequest.getAuthorId());
        
        BookResponse response = new BookResponse();
        response.setId(savedBook.getId());
        response.setIsbn(savedBook.getIsbn());
        response.setAisleNumber(savedBook.getAisleNumber());
        response.setTitle(savedBook.getTitle());
        
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(savedBook.getAuthor().getId());
        authorResponse.setFirstName(savedBook.getAuthor().getFirstName());
        authorResponse.setLastName(savedBook.getAuthor().getLastName());
        response.setAuthor(authorResponse);
        
        ResponseWrapper responseWrapped = new ResponseWrapper();
        responseWrapped.setMessage("Book added successfully");
        responseWrapped.setStatus("success");
        responseWrapped.setData(response);
        
        return new ResponseEntity<>(responseWrapped, HttpStatus.CREATED);
    }

    @GetMapping(path = "/books")
    @Operation(summary = "Get all books", description = "Returns a paginated list of all books")
    public ResponseEntity<ResponseWrapper> getBooks(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookService.getAllBooks(pageable);
        
        List<BookResponse> bookResponses = bookPage.getContent().stream().map(book -> {
            BookResponse response = new BookResponse();
            response.setId(book.getId());
            response.setTitle(book.getTitle());
            response.setIsbn(book.getIsbn());
            response.setAisleNumber(book.getAisleNumber());
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(book.getAuthor().getId());
            authorResponse.setFirstName(book.getAuthor().getFirstName());
            authorResponse.setLastName(book.getAuthor().getLastName());
            response.setAuthor(authorResponse);
            return response;
        }).collect(Collectors.toList());

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Books retrieved successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(bookResponses);

        Pagination pagination = new Pagination();
        pagination.setCurrentPage(bookPage.getNumber());
        pagination.setPageSize(bookPage.getSize());
        pagination.setTotalElements(bookPage.getTotalElements());
        pagination.setTotalPages(bookPage.getTotalPages());
        responseWrapper.setPagination(pagination);

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    @Operation(summary = "Get a book by ID", description = "Returns a book by its ID")
    public ResponseEntity<ResponseWrapper> getBookById(@PathVariable(value = "id") Integer id) {
        Book book = bookService.getBookById(id);
        
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setIsbn(book.getIsbn());
        response.setAisleNumber(book.getAisleNumber());
        
        AuthorResponse author = new AuthorResponse();
        author.setId(book.getAuthor().getId());
        author.setFirstName(book.getAuthor().getFirstName());
        author.setLastName(book.getAuthor().getLastName());
        response.setAuthor(author);
        
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Book retrieved successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(response);
        
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @PutMapping("/books/{id}")
    @Operation(summary = "Update a book", description = "Updates an existing book's details")
    public ResponseEntity<ResponseWrapper> updateBook(@PathVariable(value = "id") Integer id, @Valid @RequestBody BookRequest bookRequest) {
        Book bookDetails = new Book();
        bookDetails.setIsbn(bookRequest.getIsbn());
        bookDetails.setTitle(bookRequest.getTitle());
        bookDetails.setAisleNumber(bookRequest.getAisleNumber());
        
        Book updatedBook = bookService.updateBook(id, bookDetails, bookRequest.getAuthorId());
        
        BookResponse response = new BookResponse();
        response.setId(updatedBook.getId());
        response.setTitle(updatedBook.getTitle());
        response.setIsbn(updatedBook.getIsbn());
        response.setAisleNumber(updatedBook.getAisleNumber());
        
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(updatedBook.getAuthor().getId());
        authorResponse.setFirstName(updatedBook.getAuthor().getFirstName());
        authorResponse.setLastName(updatedBook.getAuthor().getLastName());
        response.setAuthor(authorResponse);
        
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Book updated successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(response);
        
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    public ResponseEntity<ResponseWrapper> deleteBook(@PathVariable(value = "id") Integer id) {
        bookService.deleteBook(id);
        
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Book deleted successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(new BookResponse());
        
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @GetMapping(path = "/authors")
    @Operation(summary = "Get all authors", description = "Returns a paginated list of all authors")
    public ResponseEntity<ResponseWrapper> getAuthors(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Author> authorPage = authorService.getAllAuthors(pageable);

        List<AuthorResponse> authorResponses = authorPage.getContent().stream().map(author -> {
            AuthorResponse response = new AuthorResponse();
            response.setId(author.getId());
            response.setFirstName(author.getFirstName());
            response.setLastName(author.getLastName());
            return response;
        }).collect(Collectors.toList());

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Authors retrieved successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(authorResponses);
        
        Pagination pagination = new Pagination();
        pagination.setCurrentPage(authorPage.getNumber());
        pagination.setPageSize(authorPage.getSize());
        pagination.setTotalElements(authorPage.getTotalElements());
        pagination.setTotalPages(authorPage.getTotalPages());
        responseWrapper.setPagination(pagination);

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @GetMapping("/authors/{id}")
    @Operation(summary = "Get an author by ID", description = "Returns an author by their ID")
    public ResponseEntity<ResponseWrapper> getAuthorById(@PathVariable(value = "id") Integer id) {
        Author author = authorService.getAuthorById(id);
        
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(author.getId());
        authorResponse.setFirstName(author.getFirstName());
        authorResponse.setLastName(author.getLastName());
        
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Author retrieved successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(authorResponse);
        
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @PostMapping("/authors")
    @Operation(summary = "Add a new author", description = "Creates a new author with the given details") 
    public ResponseEntity<ResponseWrapper> addAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        Author author = new Author();
        author.setFirstName(authorRequest.getFirstName());
        author.setLastName(authorRequest.getLastName());
        
        Author savedAuthor = authorService.addAuthor(author);
        
        AuthorResponse response = new AuthorResponse();
        response.setId(savedAuthor.getId());
        response.setFirstName(savedAuthor.getFirstName());
        response.setLastName(savedAuthor.getLastName());
        
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Author added successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(response);
        
        return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
    }

    @PutMapping("/authors/{id}")
    @Operation(summary = "Update an author", description = "Updates an existing author's details")
    public ResponseEntity<ResponseWrapper> updateAuthor(@PathVariable(value = "id") Integer id, @Valid @RequestBody AuthorRequest authorRequest) {
        Author authorDetails = new Author();
        authorDetails.setFirstName(authorRequest.getFirstName());
        authorDetails.setLastName(authorRequest.getLastName());
        
        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
        
        AuthorResponse response = new AuthorResponse();
        response.setId(updatedAuthor.getId());
        response.setFirstName(updatedAuthor.getFirstName());
        response.setLastName(updatedAuthor.getLastName());
        
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(response);
        responseWrapper.setStatus("success");
        responseWrapper.setMessage("Author updated successfully");
        
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @DeleteMapping("/authors/{id}")
    @Operation(summary = "Delete an author", description = "Deletes an author by their ID")
    public ResponseEntity<ResponseWrapper> deleteAuthor(@PathVariable(value = "id") Integer id) {
        authorService.deleteAuthor(id);
        
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatus("success");
        responseWrapper.setData(new AuthorResponse());
        responseWrapper.setMessage("Author deleted successfully");
        
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }
    
}
