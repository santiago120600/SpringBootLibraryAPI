package com.api.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.ResponseWrapper;
import com.api.library.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Author API", description = "API endpoints for managing authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping(path = "/authors")
    @Operation(summary = "Get all authors", description = "Returns a paginated list of all authors")
    public ResponseEntity<ResponseWrapper> getAuthors(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(authorService.getAllAuthors(pageable), HttpStatus.OK);
    }

    @GetMapping("/authors/{id}")
    @Operation(summary = "Get an author by ID", description = "Returns an author by their ID")
    public ResponseEntity<ResponseWrapper> getAuthorById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(authorService.getAuthorById(id), HttpStatus.OK);
    }

    @PostMapping("/authors")
    @Operation(summary = "Add a new author", description = "Creates a new author with the given details")
    public ResponseEntity<ResponseWrapper> addAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        return new ResponseEntity<>(authorService.addAuthor(authorRequest), HttpStatus.CREATED);
    }

    @PutMapping("/authors/{id}")
    @Operation(summary = "Update an author", description = "Updates an existing author's details")
    public ResponseEntity<ResponseWrapper> updateAuthor(@PathVariable(value = "id") Integer id,
            @Valid @RequestBody AuthorRequest authorRequest) {
        return new ResponseEntity<>(authorService.updateAuthor(id, authorRequest), HttpStatus.OK);
    }

    @DeleteMapping("/authors/{id}")
    @Operation(summary = "Delete an author", description = "Deletes an author by their ID")
    public ResponseEntity<ResponseWrapper> deleteAuthor(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(authorService.deleteAuthor(id), HttpStatus.OK);
    }
}
