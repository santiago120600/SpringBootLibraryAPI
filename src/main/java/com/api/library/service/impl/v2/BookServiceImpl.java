package com.api.library.service.impl.v2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.AuthorResponse;
import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.dto.Pagination;
import com.api.library.dto.ResponseWrapper;
import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;
import com.api.library.service.v2.BookService;

import static com.api.library.repository.BookSpecs.filterBy;

@Service("bookServiceImplV2")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public ResponseWrapper addBook(BookRequest bookRequest, Integer authorId) {
        Book existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
        if (existingBook == null) {
            Book savedBook = bookRepository.save(mapToEntity(bookRequest));
            return wrap("Book added successfully", "success", mapToDTO(savedBook));
        } else {
            return wrap("Book already exists", "failure", mapToDTO(existingBook));
        }
    }

    @Override
    public ResponseWrapper getAllBooks(Pageable pageable, BookRequest.BookRequestBuilder filterBuilder) {
        BookRequest filter = filterBuilder.build();
        Specification<Book> spec = filterBy(filter.getAisleNumber(), filter.getIsbn(), filter.getTitle());
        Page<Book> bookPage = bookRepository.findAll(spec, pageable);
        List<BookResponse> bookResponses = bookPage.getContent().stream().map(book -> {
            return mapToDTO(book);
        }).collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .currentPage(bookPage.getNumber())
                .pageSize(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .build();

        return wrap("Books retrieved successfully", "success", bookResponses, pagination);
    }

    @Override
    public ResponseWrapper getBookById(Integer id) {
        try {
            Book book = bookRepository.findById(id).get();
            return wrap("Book retrieved successfully", "success", mapToDTO(book));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseWrapper updateBook(Integer id, BookRequest bookRequest) {
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
            return wrap("Book updated successfully", "success", mapToDTO(book));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseWrapper deleteBook(Integer id) {
        try {
            Book book = bookRepository.findById(id).get();
            bookRepository.delete(book);
            BookResponse response = new BookResponse();
            return wrap("Book deleted successfully", "success", response);
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

    private ResponseWrapper wrap(String msg, String status, Object data) {
        return wrap(msg, status, data, null);
    }

    private ResponseWrapper wrap(String msg, String status,Object data, Pagination p) {
        return ResponseWrapper.builder()
                .message(msg)
                .status(status)
                .data(data)
                .pagination(p)
                .build();
    }
}
