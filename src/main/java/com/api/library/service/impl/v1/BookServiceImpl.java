package com.api.library.service.impl.v1;

import static com.api.library.repository.BookSpecs.filterBy;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.exception.ResourceNotFoundException;
import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;
import com.api.library.service.v1.BookService;

import lombok.AllArgsConstructor;

@Service("bookServiceImplV1")
@Transactional
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    private AuthorRepository authorRepository;

    private ModelMapper modelMapper;

    @Override
    public BookResponse addBook(BookRequest bookRequest, Integer authorId) {
        Book existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
        if (existingBook == null) {
            Book book = Book.builder().aisleNumber(bookRequest.getAisleNumber()).title(bookRequest.getTitle()).isbn(bookRequest.getIsbn()).build();
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Author", "id", authorId));
            book.setAuthor(author);
            return modelMapper.map(bookRepository.save(book), BookResponse.class);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks(BookRequest.BookRequestBuilder filterBuilder) {
        BookRequest filter = filterBuilder.build();
        Specification<Book> spec = filterBy(filter.getAisleNumber(), filter.getIsbn(), filter.getTitle());
        List<Book> books = bookRepository.findAll(spec);

        return books.stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return modelMapper.map(book, BookResponse.class);
    }

    @Override
    public BookResponse updateBook(Integer id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setAisleNumber(bookRequest.getAisleNumber());
        book.setIsbn(bookRequest.getIsbn());
        book.setTitle(bookRequest.getTitle());
        Author author = authorRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        ;
        book.setAuthor(author);
        return modelMapper.map(bookRepository.save(book), BookResponse.class);
    }

    @Override
    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        bookRepository.delete(book);
    }
}
