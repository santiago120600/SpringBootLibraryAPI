package com.api.library.service.v2;

import java.util.List;

import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.dto.ResponseWrapper;

public interface BookService {

    ResponseWrapper<BookResponse> addBook(BookRequest book, Integer authorId);

    ResponseWrapper<List<BookResponse>> getAllBooks(BookRequest.BookRequestBuilder filterBuilder, Integer page, Integer size, String sortBy, String sortOrder);

    ResponseWrapper<BookResponse> getBookById(Integer id);

    ResponseWrapper<BookResponse> updateBook(Integer id, BookRequest bookDetails);

    ResponseWrapper<BookResponse> deleteBook(Integer id);
}
