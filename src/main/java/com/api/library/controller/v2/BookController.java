package com.api.library.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.api.library.dto.BookRequest;
import com.api.library.dto.ResponseWrapper;
import com.api.library.service.v2.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("v2BookController")
@RequestMapping("/api/v2/books")
@Tag(name = "Book API V2", description = "API endpoints for managing books - Version 2")
public class BookController {

    @Autowired
    @Qualifier("bookServiceImplV2")
    private BookService bookService;

    @PostMapping
    @Operation(summary = "Add a new book", description = "Creates a new book with the given details")
    public ResponseEntity<ResponseWrapper> addBook(@Valid @RequestBody BookRequest bookRequest) {
        return new ResponseEntity<>(bookService.addBook(bookRequest, bookRequest.getAuthorId()), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns a paginated list of all books")
    public ResponseEntity<ResponseWrapper> getBooks(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "isbn", required = false) String isbn,
            @RequestParam(name = "aisleNumber", required = false) Integer aisleNumber,
            @RequestParam(name = "sort", defaultValue = "aisleNumber") String sort
        ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return new ResponseEntity<>(bookService.getAllBooks(pageable, BookRequest.builder().aisleNumber(aisleNumber).title(title).isbn(isbn)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Returns a book by its ID")
    public ResponseEntity<ResponseWrapper> getBookById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Updates an existing book's details")
    public ResponseEntity<ResponseWrapper> updateBook(@PathVariable(value = "id") Integer id, @Valid @RequestBody BookRequest bookRequest) {
        return new ResponseEntity<>(bookService.updateBook(id, bookRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    public ResponseEntity<ResponseWrapper> deleteBook(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }
    
}
