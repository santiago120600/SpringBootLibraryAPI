package com.api.library.service.v2;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.ResponseWrapper;
import com.api.library.model.Author;

public interface AuthorService {

    ResponseWrapper addAuthor(AuthorRequest author);

    ResponseWrapper getAllAuthors(Author.AuthorBuilder filterBuilder, Integer page, Integer size, String sortBy, String sortOrder);

    ResponseWrapper getAuthorById(Integer id);

    ResponseWrapper updateAuthor(Integer id, AuthorRequest authorDetails);

    ResponseWrapper deleteAuthor(Integer id);
}
