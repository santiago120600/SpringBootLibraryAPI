package com.api.library.service.v2;

import com.api.library.dto.ResponseWrapper;
import com.api.library.model.Author;

public interface AuthorService {

    Author addAuthor(Author author);

    ResponseWrapper getAllAuthors(Author.AuthorBuilder filterBuilder, Integer page, Integer size, String sortBy, String sortOrder);

    Author getAuthorById(Integer id);

    Author updateAuthor(Integer id, Author authorDetails);

    void deleteAuthor(Integer id);
}
