package com.api.library.service.v2;

import java.util.List;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.dto.ResponseWrapper;

public interface AuthorService {

    ResponseWrapper<AuthorResponse> addAuthor(AuthorRequest author);

    ResponseWrapper<List<AuthorResponse>> getAllAuthors(AuthorRequest.AuthorRequestBuilder filterBuilder, Integer page, Integer size, String sortBy, String sortOrder);

    ResponseWrapper<AuthorResponse> getAuthorById(Integer id);

    ResponseWrapper<AuthorResponse> updateAuthor(Integer id, AuthorRequest authorDetails);

    ResponseWrapper<AuthorResponse> deleteAuthor(Integer id);
}
