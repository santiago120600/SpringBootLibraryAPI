package com.api.library.controller.v2;

import java.util.List;

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

import com.api.library.config.AppConstants;
import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.dto.ResponseWrapper;
import com.api.library.service.v2.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("v2BookController")
@RequestMapping("/api/v2/books")
@AllArgsConstructor
@Tag(name = "Book API V2", description = "API endpoints for managing books - Version 2")
public class BookController {

    @Qualifier("bookServiceImplV2")
    private BookService bookService;

    @PostMapping
    @Operation(summary = "Add a new book", description = "Creates a new book with the given details")
    public ResponseEntity<ResponseWrapper<BookResponse>> addBook(@Valid @RequestBody BookRequest bookRequest) {
            return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(
                            BookRequest.builder().title(bookRequest.getTitle()).isbn(bookRequest.getIsbn())
                                            .aisleNumber(bookRequest.getAisleNumber()).build(),
                            bookRequest.getAuthorId()));
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns a paginated list of all books")
    public ResponseEntity<ResponseWrapper<List<BookResponse>>> getBooks(
            @RequestParam(value = "page", defaultValue = AppConstants.PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) Integer size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "isbn", required = false) String isbn,
            @RequestParam(name = "aisleNumber", required = false) Integer aisleNumber,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BOOK_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
            return new ResponseEntity<>(bookService.getAllBooks(
                            BookRequest.builder().aisleNumber(aisleNumber).title(title).isbn(isbn), page, size, sortBy,
                            sortOrder), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Returns a book by its ID")
    public ResponseEntity<ResponseWrapper<BookResponse>> getBookById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Updates an existing book's details")
    public ResponseEntity<ResponseWrapper<BookResponse>> updateBook(@PathVariable(value = "id") Integer id,
            @Valid @RequestBody BookRequest bookRequest) {
        return new ResponseEntity<>(bookService.updateBook(id, bookRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    public ResponseEntity<ResponseWrapper<BookResponse>> deleteBook(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }
    
}
