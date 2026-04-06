package com.api.library.service.v1;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;
import com.api.library.service.impl.v1.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void BookService_CreateBook_ReturnsBookDto() {
        Author author = Author.builder()
        .firstName("Juan")
        .lastName("Balderas")
        .build();
        Book book = Book.builder()
        .title("The Great Gatsby")
        .isbn("978-3-16-148410-0")
        .aisleNumber(1)
        .author(author)
        .build();

        BookRequest bookRequest = BookRequest.builder()
        .title("The Great Gatsby")
        .isbn("978-3-16-148410-0")
        .aisleNumber(1)
        .authorId(1)
        .build();

        when(authorRepository.findById(1)).thenReturn(java.util.Optional.of(author));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
        BookResponse savedBook = bookService.addBook(bookRequest, 1);
        Assertions.assertNotNull(savedBook);
    }

}