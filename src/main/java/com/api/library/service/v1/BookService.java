package com.api.library.service.v1;

import java.util.List;

import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;

public interface BookService {

    BookResponse addBook(BookRequest book, Integer authorId);

    List<BookResponse> getAllBooks();

    BookResponse getBookById(Integer id);

    BookResponse updateBook(Integer id, BookRequest bookDetails);

    void deleteBook(Integer id);
}
