package com.api.library.service.impl.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.AuthorResponse;
import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;
import com.api.library.service.v1.BookService;

@Service("bookServiceImplV1")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public BookResponse addBook(BookRequest bookRequest, Integer authorId) {
        Book existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
        if (existingBook == null) {
            Book savedBook = bookRepository.save(mapToEntity(bookRequest));
            return mapToDTO(savedBook);
        } else {
            return mapToDTO(existingBook);
        }
    }

    @Override
    public List<BookResponse> getAllBooks() {
        List<BookResponse> bookResponses = bookRepository.findAll().stream().map(book -> {
            return mapToDTO(book);
        }).collect(Collectors.toList());

        return bookResponses;
    }

    @Override
    public BookResponse getBookById(Integer id) {
        try {
            Book book = bookRepository.findById(id).get();
            return mapToDTO(book);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public BookResponse updateBook(Integer id, BookRequest bookRequest) {
        try {
            Book book = bookRepository.findById(id).get();
            book.setAisleNumber(bookRequest.getAisleNumber());
            book.setIsbn(bookRequest.getIsbn());
            book.setTitle(bookRequest.getTitle());
            Author author = authorRepository.findById(bookRequest.getAuthorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
            ;
            book.setAuthor(author);
            bookRepository.save(book);
            return mapToDTO(book);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteBook(Integer id) {
        try {
            Book book = bookRepository.findById(id).get();
            bookRepository.delete(book);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private BookResponse mapToDTO(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAisleNumber(book.getAisleNumber());
        response.setIsbn(book.getIsbn());
        AuthorResponse author = new AuthorResponse();
        author.setId(book.getAuthor().getId());
        author.setFirstName(book.getAuthor().getFirstName());
        author.setLastName(book.getAuthor().getLastName());
        response.setAuthor(author);
        return response;
    }

    private Book mapToEntity(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAisleNumber(bookRequest.getAisleNumber());
        book.setIsbn(bookRequest.getIsbn());
        Author author = authorRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
        book.setAuthor(author);
        return book;
    } 
}
