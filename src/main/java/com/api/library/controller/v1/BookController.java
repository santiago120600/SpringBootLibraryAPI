package com.api.library.controller.v1;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.model.Book;
import com.api.library.service.v1.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("v1BookController")
@RequestMapping("/api/v1/books")
@Tag(name = "Book API V1", description = "API endpoints for managing books - Version 1")
@AllArgsConstructor
public class BookController {

    @Qualifier("bookServiceImplV1")
    private BookService bookService;

    private ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Add a new book", description = "Creates a new book with the given details")
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookRequest bookRequest) {
        Book book = modelMapper.map(bookRequest, Book.class);
        return new ResponseEntity<>(modelMapper.map(bookService.addBook(book, book.getAuthor().getId()), BookResponse.class), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns a paginated list of all books")
    public ResponseEntity<List<BookResponse>> getBooks(
        @RequestParam(name = "title", required = false) String title,
        @RequestParam(name = "isbn", required = false) String isbn,
        @RequestParam(name = "aisleNumber", required = false) Integer aisleNumber
    ) {
        return new ResponseEntity<>(bookService.getAllBooks(Book.builder().aisleNumber(aisleNumber).title(title).isbn(isbn))
            .stream()
            .map(book -> modelMapper.map(book, BookResponse.class))
            .toList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Returns a book by its ID")
    public ResponseEntity<?> getBookById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(modelMapper.map(bookService.getBookById(id), BookResponse.class), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Updates an existing book's details")
    public ResponseEntity<?> updateBook(@PathVariable(value = "id") Integer id, @Valid @RequestBody BookRequest bookRequest) {
        Book book = modelMapper.map(bookRequest, Book.class);
        return new ResponseEntity<>(modelMapper.map(bookService.updateBook(id, book), BookResponse.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    public ResponseEntity<?> deleteBook(@PathVariable(value = "id") Integer id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
