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

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.ResponseWrapper;
import com.api.library.service.v2.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("v2AuthorController")
@RequestMapping("/api/v2/authors")
@Tag(name = "Author API V2", description = "API endpoints for managing authors - Version 2")
public class AuthorController {

    @Autowired
    @Qualifier("authorServiceImplV2")
    private AuthorService authorService;

    @GetMapping
    @Operation(summary = "Get all authors", description = "Returns a paginated list of all authors")
    public ResponseEntity<ResponseWrapper> getAuthors(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "sort", defaultValue = "lastName") String sort
        ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return new ResponseEntity<>(authorService.getAllAuthors(pageable, AuthorRequest.builder().firstName(firstName).lastName(lastName)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID", description = "Returns an author by their ID")
    public ResponseEntity<ResponseWrapper> getAuthorById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(authorService.getAuthorById(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Add a new author", description = "Creates a new author with the given details")
    public ResponseEntity<ResponseWrapper> addAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        return new ResponseEntity<>(authorService.addAuthor(authorRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an author", description = "Updates an existing author's details")
    public ResponseEntity<ResponseWrapper> updateAuthor(@PathVariable(value = "id") Integer id,
            @Valid @RequestBody AuthorRequest authorRequest) {
        return new ResponseEntity<>(authorService.updateAuthor(id, authorRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author", description = "Deletes an author by their ID")
    public ResponseEntity<ResponseWrapper> deleteAuthor(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(authorService.deleteAuthor(id), HttpStatus.OK);
    }
}
