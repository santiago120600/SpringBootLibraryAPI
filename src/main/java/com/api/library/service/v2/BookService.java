package com.api.library.service.v2;

import org.springframework.data.domain.Pageable;

import com.api.library.dto.BookRequest;
import com.api.library.dto.ResponseWrapper;

public interface BookService {

    ResponseWrapper  addBook(BookRequest book, Integer authorId);

    ResponseWrapper  getAllBooks(Pageable pageable, BookRequest.BookRequestBuilder filterBuilder);

    ResponseWrapper  getBookById(Integer id);

    ResponseWrapper  updateBook(Integer id, BookRequest bookDetails);

    ResponseWrapper  deleteBook(Integer id);
}
