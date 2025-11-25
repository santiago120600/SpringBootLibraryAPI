package com.api.library.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.service.v1.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("v1AuthorController")
@RequestMapping("/api/v1/authors")
@Tag(name = "Author API V1", description = "API endpoints for managing authors - Version 1")
public class AuthorController {

    @Autowired
    @Qualifier("authorServiceImplV1")
    private AuthorService authorService;

    @GetMapping
    @Operation(summary = "Get all authors", description = "Returns list of all authors")
    public ResponseEntity<List<AuthorResponse>> getAuthors(
        @RequestParam(name = "firstName", required = false) String firstName,
        @RequestParam(name = "lastName", required = false) String lastName
    ) {
        return new ResponseEntity<>(authorService.getAllAuthors(AuthorRequest.builder().firstName(firstName).lastName(lastName)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID", description = "Returns an author by their ID")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(authorService.getAuthorById(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Add a new author", description = "Creates a new author with the given details")
    public ResponseEntity<AuthorResponse> addAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        return new ResponseEntity<>(authorService.addAuthor(authorRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an author", description = "Updates an existing author's details")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable(value = "id") Integer id,
            @Valid @RequestBody AuthorRequest authorRequest) {
        return new ResponseEntity<>(authorService.updateAuthor(id, authorRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author", description = "Deletes an author by their ID")
    public ResponseEntity<AuthorResponse> deleteAuthor(@PathVariable(value = "id") Integer id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
