package com.api.library.service.v2;

import org.springframework.data.domain.Pageable;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.ResponseWrapper;

public interface AuthorService {

    ResponseWrapper addAuthor(AuthorRequest author);

    ResponseWrapper  getAllAuthors(Pageable pageable, AuthorRequest.AuthorRequestBuilder filterBuilder);

    ResponseWrapper  getAuthorById(Integer id);

    ResponseWrapper  updateAuthor(Integer id, AuthorRequest authorDetails);

    ResponseWrapper  deleteAuthor(Integer id);
}
