package com.api.library.controller.v2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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
import com.api.library.dto.BookResponse;
import com.api.library.dto.Pagination;
import com.api.library.dto.ResponseWrapper;
import com.api.library.model.Book;
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

    private ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Add a new book", description = "Creates a new book with the given details")
    public ResponseEntity<ResponseWrapper> addBook(@Valid @RequestBody BookRequest bookRequest) {
        Book book = bookService.addBook(
                Book.builder().title(bookRequest.getTitle()).isbn(bookRequest.getIsbn())
                        .aisleNumber(bookRequest.getAisleNumber()).build(),
                bookRequest.getAuthorId());
        ResponseWrapper response = ResponseWrapper.builder().message("Book added successfully").status("success")
                .data(modelMapper.map(book, BookResponse.class)).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Returns a paginated list of all books")
    public ResponseEntity<ResponseWrapper> getBooks(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "isbn", required = false) String isbn,
            @RequestParam(name = "aisleNumber", required = false) Integer aisleNumber,
            @RequestParam(name = "sort", defaultValue = "aisleNumber") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Book> book = bookService.getAllBooks(pageable,
                Book.builder().aisleNumber(aisleNumber).title(title).isbn(isbn));

        Pagination pagination = new Pagination();
        pagination.setCurrentPage(book.getPageable().getPageNumber());
        pagination.setPageSize(book.getSize());
        pagination.setTotalElements(book.getTotalElements());
        pagination.setTotalPages(book.getTotalPages());

        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Books retrieved successfully");
        response.setStatus("success");
        response.setPagination(pagination);
        response.setData(book
                .map(item -> modelMapper.map(item, BookResponse.class))
                .getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Returns a book by its ID")
    public ResponseEntity<ResponseWrapper> getBookById(@PathVariable(value = "id") Integer id) {
        BookResponse book = modelMapper.map(bookService.getBookById(id), BookResponse.class);
        ResponseWrapper response = ResponseWrapper.builder().message("Book retrieved successfully").status("success")
        .data(book).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Updates an existing book's details")
    public ResponseEntity<ResponseWrapper> updateBook(@PathVariable(value = "id") Integer id,
            @Valid @RequestBody BookRequest bookRequest) {
        Book book = bookService.updateBook(id, modelMapper.map(bookRequest, Book.class));
        ResponseWrapper response = ResponseWrapper.builder().message("Book updated successfully").status("success")
                .data(modelMapper.map(book, BookResponse.class)).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book by its ID")
    public ResponseEntity<ResponseWrapper> deleteBook(@PathVariable(value = "id") Integer id) {
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Book deleted successfully");
        response.setStatus("success");
        bookService.deleteBook(id);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
    
}
