package com.api.library.service.v1;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    private final String TITLE = "The Great Gatsby";
    private final String ISBN = "978-3-16-148410-0";
    private final Integer AISLE_NUMBER = 1;
    private final Integer AUTHOR_ID = 1;
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private Author author;
    private Book book;
    private BookRequest bookRequest;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        author = createAuthor(AUTHOR_ID, FIRST_NAME, LAST_NAME);
        book = createBook(TITLE, ISBN, AISLE_NUMBER, author);
        bookRequest = createBookRequest(TITLE, ISBN, AISLE_NUMBER, AUTHOR_ID);
    }

    private Author createAuthor(Integer id, String firstName, String lastName) {
        return Author.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    private Book createBook(String title, String isbn, Integer aisleNumber, Author author) {
        return Book.builder()
                .title(title)
                .isbn(isbn)
                .aisleNumber(aisleNumber)
                .author(author)
                .build();
    }

    private BookRequest createBookRequest(String title, String isbn, Integer aisleNumber, Integer authorId) {
        return BookRequest.builder()
                .title(title)
                .isbn(isbn)
                .aisleNumber(aisleNumber)
                .authorId(authorId)
                .build();
    }

    @Test
    public void BookService_CreateBook_ReturnsBookDto() {
        when(bookRepository.findByIsbn(ISBN)).thenReturn(null);
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(java.util.Optional.of(author));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        BookResponse savedBook = bookService.addBook(bookRequest, AUTHOR_ID);
        Assertions.assertNotNull(savedBook);
        Assertions.assertEquals(book.getTitle(), savedBook.getTitle());
        Assertions.assertEquals(book.getIsbn(), savedBook.getIsbn());
        Assertions.assertEquals(book.getAisleNumber(), savedBook.getAisleNumber());
        Assertions.assertEquals(book.getAuthor().getFirstName(), savedBook.getAuthor().getFirstName());
        Assertions.assertEquals(book.getAuthor().getLastName(), savedBook.getAuthor().getLastName());
        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(bookRepository, times(1)).findByIsbn(ISBN);
        verify(bookRepository, times(1)).save(Mockito.any(Book.class));
    }

}