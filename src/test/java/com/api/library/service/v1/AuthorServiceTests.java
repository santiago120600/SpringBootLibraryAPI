package com.api.library.service.v1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.impl.v1.AuthorServiceImpl;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class AuthorServiceTests {

    private final Integer AUTHOR_ID = 1;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @ParameterizedTest
    @CsvSource({
            "Juan, Balderas"
    })
    public void AuthorService_AddAuthor_ReturnsAuthorDto(String firstName, String lastName) {
        Author author = Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        AuthorRequest authorDto = AuthorRequest.builder().firstName(firstName).lastName(lastName).build();

        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);
        AuthorResponse savedAuthor = authorService.addAuthor(authorDto);
        Assertions.assertNotNull(savedAuthor);
        Assertions.assertEquals(author.getFirstName(), savedAuthor.getFirstName());
        Assertions.assertEquals(author.getLastName(), savedAuthor.getLastName());
    }

    @Test
    public void AuthorService_GetAllAuthors__WithBothFirstNameAndLastName_ShouldReturnFilteredAuthors() {
        String firstName = "John";
        String lastName = "Doe";

        AuthorRequest.AuthorRequestBuilder filterBuilder = AuthorRequest.builder()
                .firstName(firstName)
                .lastName(lastName);

        Author author1 = Author.builder()
                .id(1)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        Author author2 = Author.builder()
                .id(2)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        List<Author> authors = Arrays.asList(author1, author2);

        when(authorRepository.findAll(any(Specification.class))).thenReturn(authors);

        List<AuthorResponse> result = authorService.getAllAuthors(filterBuilder);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(firstName, result.get(0).getFirstName());
        assertEquals(lastName, result.get(0).getLastName());
        verify(authorRepository, times(1)).findAll(any(Specification.class));
    }

    @ParameterizedTest
    @CsvSource({
            "Juan, Balderas"
    })
    public void AuthorService_GetAuthorById_ReturnsAuthorDto(String firstName, String lastName) {
        Author author = Author.builder()
                .id(AUTHOR_ID)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.ofNullable(author));
        AuthorResponse authorResponse = authorService.getAuthorById(AUTHOR_ID);
        Assertions.assertNotNull(authorResponse);
        Assertions.assertEquals(author.getId(), authorResponse.getId());
        Assertions.assertEquals(author.getFirstName(), authorResponse.getFirstName());
        Assertions.assertEquals(author.getLastName(), authorResponse.getLastName());
    }

    @Test
    public void AuthorService_GetAuthorById_ThrowNotFound_WhenAuthorDoesNotExist() {
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authorService.getAuthorById(AUTHOR_ID));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND", exception.getMessage());

        verify(authorRepository, times(1)).findById(AUTHOR_ID);
    }

    @ParameterizedTest
    @CsvSource({
            "Juan, Balderas"
    })
    public void AuthorService_UpdateAuthor_ReturnsAuthorDto(String firstName, String lastName) {
        Author author = Author.builder()
                .id(AUTHOR_ID)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        AuthorRequest authorDto = AuthorRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.ofNullable(author));
        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);
        AuthorResponse updatedAuthor = authorService.updateAuthor(AUTHOR_ID, authorDto);
        Assertions.assertNotNull(updatedAuthor);
        Assertions.assertEquals(author.getId(), updatedAuthor.getId());
        Assertions.assertEquals(author.getFirstName(), updatedAuthor.getFirstName());
        Assertions.assertEquals(author.getLastName(), updatedAuthor.getLastName());
    }

    @ParameterizedTest
    @CsvSource({
            "Juan, Balderas"
    })
    public void AuthorService_UpdateAuthor_ThrowNotFound_WhenAuthorDoesNotExist(String firstName, String lastName) {
        AuthorRequest authorDto = AuthorRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authorService.updateAuthor(AUTHOR_ID, authorDto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND", exception.getMessage());

        verify(authorRepository, times(1)).findById(AUTHOR_ID);
    }

    @ParameterizedTest
    @CsvSource({
            "Juan, Balderas"
    })
    public void AuthorService_DeleteAuthor_ReturnsVoid(String firstName, String lastName) {
        Author author = Author.builder()
                .id(AUTHOR_ID)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.ofNullable(author));
        doNothing().when(authorRepository).delete(author);
        assertAll(() -> authorService.deleteAuthor(AUTHOR_ID));
    }

    @Test
    public void AuthorService_DeleteAuthor_ThrowNotFound_WhenAuthorDoesNotExist() {
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authorService.deleteAuthor(AUTHOR_ID));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND", exception.getMessage());

        // Verify delete was never called
        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(authorRepository, never()).delete(any(Author.class));
    }
}
