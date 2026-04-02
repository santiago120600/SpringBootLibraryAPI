package com.api.library.service.v1;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.impl.v1.AuthorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTests {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void AuthorService_CreateAuthor_ReturnsAuthorDto() {
       Author author = Author.builder()
        .firstName("Juan")
        .lastName("Balderas")
        .build();
        AuthorRequest authorDto = AuthorRequest.builder().firstName("Juan").lastName("Balderas").build();

        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);
        AuthorResponse savedAuthor = authorService.addAuthor(authorDto);
        Assertions.assertNotNull(savedAuthor);
    }
}
