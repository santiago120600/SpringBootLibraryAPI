package com.api.library.service.impl.v2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public ResponseWrapper addBook(BookRequest bookRequest, Integer authorId) {
        Book existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
        ResponseWrapper responseWrapped = new ResponseWrapper();
        if (existingBook == null) {
            Book savedBook = bookRepository.save(mapToEntity(bookRequest));
            responseWrapped.setMessage("Book added successfully");
            responseWrapped.setStatus("success");
            responseWrapped.setData(mapToDTO(savedBook));
        } else {
            responseWrapped.setMessage("Book already exists");
            responseWrapped.setStatus("failure");
            responseWrapped.setData(mapToDTO(existingBook));
        }
        return responseWrapped;
    }

    @Override
    public ResponseWrapper getAllBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        List<BookResponse> bookResponses = bookPage.getContent().stream().map(book -> {
            return mapToDTO(book);
        }).collect(Collectors.toList());

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Books retrieved successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(bookResponses);

        Pagination pagination = new Pagination();
        pagination.setCurrentPage(bookPage.getNumber());
        pagination.setPageSize(bookPage.getSize());
        pagination.setTotalElements(bookPage.getTotalElements());
        pagination.setTotalPages(bookPage.getTotalPages());
        responseWrapper.setPagination(pagination);

        return responseWrapper;
    }

    @Override
    public ResponseWrapper getBookById(Integer id) {
        try {
            Book book = bookRepository.findById(id).get();
            ResponseWrapper responseWrapper = new ResponseWrapper();
            responseWrapper.setMessage("Book retrieved successfully");
            responseWrapper.setStatus("success");
            responseWrapper.setData(mapToDTO(book));
            return responseWrapper;
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
            ResponseWrapper responseWrapper = new ResponseWrapper();
            responseWrapper.setMessage("Book updated successfully");
            responseWrapper.setStatus("success");
            responseWrapper.setData(mapToDTO(book));
            return responseWrapper;
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
            ResponseWrapper responseWrapper = new ResponseWrapper();
            responseWrapper.setMessage("Book deleted successfully");
            responseWrapper.setStatus("success");
            responseWrapper.setData(response);
            return responseWrapper;
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
