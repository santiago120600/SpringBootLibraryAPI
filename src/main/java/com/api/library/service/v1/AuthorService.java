package com.api.library.service.v1;

import java.util.List;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;

public interface AuthorService {

    AuthorResponse addAuthor(AuthorRequest author);

    List<AuthorResponse> getAllAuthors(AuthorRequest.AuthorRequestBuilder filter);

    AuthorResponse getAuthorById(Integer id);

    AuthorResponse updateAuthor(Integer id, AuthorRequest authorDetails);

    void deleteAuthor(Integer id);
}
