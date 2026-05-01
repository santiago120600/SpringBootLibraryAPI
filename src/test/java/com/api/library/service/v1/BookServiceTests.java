package com.api.library.service.v1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse savedBook = bookService.addBook(bookRequest, AUTHOR_ID);
        Assertions.assertNotNull(savedBook);
        Assertions.assertEquals(book.getTitle(), savedBook.getTitle());
        Assertions.assertEquals(book.getIsbn(), savedBook.getIsbn());
        Assertions.assertEquals(book.getAisleNumber(), savedBook.getAisleNumber());
        Assertions.assertEquals(book.getAuthor().getFirstName(), savedBook.getAuthor().getFirstName());
        Assertions.assertEquals(book.getAuthor().getLastName(), savedBook.getAuthor().getLastName());
        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(bookRepository, times(1)).findByIsbn(ISBN);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void BookService_CreateBook_ReturnsExistingBook_WhenBookAlreadyExists() {
        when(bookRepository.findByIsbn(ISBN)).thenReturn(book);

        BookResponse savedBook = bookService.addBook(bookRequest, AUTHOR_ID);
        Assertions.assertNotNull(savedBook);
        Assertions.assertEquals(book.getTitle(), savedBook.getTitle());
        Assertions.assertEquals(book.getIsbn(), savedBook.getIsbn());
        Assertions.assertEquals(book.getAisleNumber(), savedBook.getAisleNumber());
        Assertions.assertEquals(book.getAuthor().getFirstName(), savedBook.getAuthor().getFirstName());
        Assertions.assertEquals(book.getAuthor().getLastName(), savedBook.getAuthor().getLastName());
        verify(bookRepository, times(1)).findByIsbn(ISBN);
        verify(authorRepository, times(0)).findById(AUTHOR_ID);
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void BookService_GetAllBooks_WithTitle_ShouldReturnFilteredBooks() {
        String title = "The Great Gatsby";

        BookRequest.BookRequestBuilder filterBuilder = BookRequest.builder().title(title);

        when(bookRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(book));

        List<BookResponse> result = bookService.getAllBooks(filterBuilder);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(title, result.get(0).getTitle());
        Assertions.assertEquals(book.getIsbn(), result.get(0).getIsbn());
        Assertions.assertEquals(book.getAisleNumber(), result.get(0).getAisleNumber());
        Assertions.assertEquals(book.getAuthor().getFirstName(), result.get(0).getAuthor().getFirstName());
        Assertions.assertEquals(book.getAuthor().getLastName(), result.get(0).getAuthor().getLastName());
        verify(bookRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void BookService_GetBookById_ReturnsBookDto() {
        when(bookRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(book));
        BookResponse foundBook = bookService.getBookById(AUTHOR_ID);
        Assertions.assertNotNull(foundBook);
        Assertions.assertEquals(book.getTitle(), foundBook.getTitle());
        Assertions.assertEquals(book.getIsbn(), foundBook.getIsbn());
        Assertions.assertEquals(book.getAisleNumber(), foundBook.getAisleNumber());
        Assertions.assertEquals(book.getAuthor().getFirstName(), foundBook.getAuthor().getFirstName());
        Assertions.assertEquals(book.getAuthor().getLastName(), foundBook.getAuthor().getLastName());
        verify(bookRepository, times(1)).findById(AUTHOR_ID);
    }

    @Test
    public void BookService_GetBookById_ThrowNotFound_WhenBookDoesNotExist() {
        when(bookRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> bookService.getBookById(AUTHOR_ID));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("404 NOT_FOUND", exception.getMessage());

        verify(bookRepository, times(1)).findById(AUTHOR_ID);
    }

    @Test
    public void BookService_UpdateBook_ReturnsBookDto() {
        when(bookRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(book));
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        BookResponse updatedBook = bookService.updateBook(AUTHOR_ID, bookRequest);
        Assertions.assertNotNull(updatedBook);
        Assertions.assertEquals(book.getTitle(), updatedBook.getTitle());
        Assertions.assertEquals(book.getIsbn(), updatedBook.getIsbn());
        Assertions.assertEquals(book.getAisleNumber(), updatedBook.getAisleNumber());
        Assertions.assertEquals(book.getAuthor().getFirstName(), updatedBook.getAuthor().getFirstName());
        Assertions.assertEquals(book.getAuthor().getLastName(), updatedBook.getAuthor().getLastName());
    }

    @Test
    public void BookService_UpdateBook_ThrowNotFound_WhenBookDoesNotExist() {
        when(bookRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> bookService.updateBook(AUTHOR_ID, bookRequest));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("404 NOT_FOUND", exception.getMessage());

        verify(bookRepository, times(1)).findById(AUTHOR_ID);
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    public void BookService_DeleteBook_ReturnsVoid() {
        when(bookRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);
        assertAll(() -> bookService.deleteBook(AUTHOR_ID));
    }

    @Test
    public void BookService_DeleteBook_ThrowNotFound_WhenBookDoesNotExist() {
        when(bookRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> bookService.deleteBook(AUTHOR_ID));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("404 NOT_FOUND", exception.getMessage());

        verify(bookRepository, times(1)).findById(AUTHOR_ID);
        verify(bookRepository, times(0)).delete(any(Book.class));
    }
}