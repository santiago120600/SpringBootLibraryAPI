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

import com.api.library.config.AppConstants;
import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.dto.Pagination;
import com.api.library.dto.ResponseWrapper;
import com.api.library.model.Author;
import com.api.library.service.v2.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController("v2AuthorController")
@RequestMapping("/api/v2/authors")
@Tag(name = "Author API V2", description = "API endpoints for managing authors - Version 2")
@AllArgsConstructor
public class AuthorController {

    @Qualifier("authorServiceImplV2")
    private AuthorService authorService;

    private ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Get all authors", description = "Returns a paginated list of all authors")
    public ResponseEntity<ResponseWrapper> getAuthors(
            @RequestParam(value = "page",  defaultValue =  AppConstants.PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) Integer size,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_AUTHOR_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        return new ResponseEntity<>(authorService.getAllAuthors(Author.builder().firstName(firstName).lastName(lastName), page, size, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID", description = "Returns an author by their ID")
    public ResponseEntity<ResponseWrapper> getAuthorById(@PathVariable(value = "id") Integer id) {
        AuthorResponse author = modelMapper.map(authorService.getAuthorById(id), AuthorResponse.class);
        ResponseWrapper response = ResponseWrapper.builder().message("Author retrieved successfully").status("success").data(author).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Add a new author", description = "Creates a new author with the given details")
    public ResponseEntity<ResponseWrapper> addAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        Author author = authorService.addAuthor(modelMapper.map(authorRequest, Author.class));
        ResponseWrapper response = ResponseWrapper.builder().message("Author updated successfully").status("success")
                .data(modelMapper.map(author, AuthorResponse.class)).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an author", description = "Updates an existing author's details")
    public ResponseEntity<ResponseWrapper> updateAuthor(@PathVariable(value = "id") Integer id,
            @Valid @RequestBody AuthorRequest authorRequest) {
        Author author = authorService.updateAuthor(id, modelMapper.map(authorRequest, Author.class));
        ResponseWrapper response = ResponseWrapper.builder().message("Author added successfully").status("success")
                .data(modelMapper.map(author, AuthorResponse.class)).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author", description = "Deletes an author by their ID")
    public ResponseEntity<ResponseWrapper> deleteAuthor(@PathVariable(value = "id") Integer id) {
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Author deleted successfully");
        response.setStatus("success");
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
