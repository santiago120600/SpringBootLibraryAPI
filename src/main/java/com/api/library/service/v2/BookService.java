package com.api.library.service.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.library.model.Book;

public interface BookService {

    Book addBook(Book book, Integer authorId);

    Page<Book> getAllBooks(Pageable pageable, Book.BookBuilder filterBuilder);

    Book getBookById(Integer id);

    Book updateBook(Integer id, Book bookDetails);

    void deleteBook(Integer id);
}
