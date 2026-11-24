package com.api.library.service.impl.v2;

import static com.api.library.repository.BookSpecs.filterBy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;
import com.api.library.service.v2.BookService;

import lombok.AllArgsConstructor;

@Service("bookServiceImplV2")
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

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
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book already exists");
        }
    }

    @Override
    public Page<Book> getAllBooks(Pageable pageable, Book.BookBuilder filterBuilder) {
        Book filter = filterBuilder.build();
        Specification<Book> spec = filterBy(filter.getAisleNumber(), filter.getIsbn(), filter.getTitle());
        return bookRepository.findAll(spec, pageable);
    }

    @Override
    public Book getBookById(Integer id) {
        try {
            return bookRepository.findById(id).get();
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
            return bookRepository.save(book);
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
