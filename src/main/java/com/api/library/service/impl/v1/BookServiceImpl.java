package com.api.library.service.impl.v1;

import static com.api.library.repository.BookSpecs.filterBy;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;
import com.api.library.service.v1.BookService;

@Service("bookServiceImplV1")
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Book addBook(Book bookRequest, Integer authorId) {
        Book existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
        if (existingBook == null) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
            bookRequest.setAuthor(author);
            return bookRepository.save(bookRequest);
        } else {
            return existingBook;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks(Book.BookBuilder filterBuilder) {
        Book filter = filterBuilder.build();
        Specification<Book> spec = filterBy(filter.getAisleNumber(), filter.getIsbn(), filter.getTitle());
        List<Book> bookResponses = bookRepository.findAll(spec).stream().map(book -> {
            return book;
        }).collect(Collectors.toList());

        return bookResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(Integer id) {
        try {
            Book book = bookRepository.findById(id).get();
            return book;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Book updateBook(Integer id, Book bookRequest) {
        try {
            Book book = bookRepository.findById(id).get();
            book.setAisleNumber(bookRequest.getAisleNumber());
            book.setIsbn(bookRequest.getIsbn());
            book.setTitle(bookRequest.getTitle());
            Author author = authorRepository.findById(bookRequest.getAuthor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
            ;
            book.setAuthor(author);
            bookRepository.save(book);
            return book;
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
}
