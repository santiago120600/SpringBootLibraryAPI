package com.api.library.service.v1;

import java.util.List;

import com.api.library.model.Book;

public interface BookService {

    Book addBook(Book book, Integer authorId);

    List<Book> getAllBooks(Book.BookBuilder filter);

    Book getBookById(Integer id);

    Book updateBook(Integer id, Book bookDetails);

    void deleteBook(Integer id);
}
