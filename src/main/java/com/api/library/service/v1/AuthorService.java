package com.api.library.service.v1;

import java.util.List;

import com.api.library.model.Author;

public interface AuthorService {

    Author addAuthor(Author author);

    List<Author> getAllAuthors(Author.AuthorBuilder filter);

    Author getAuthorById(Integer id);

    Author updateAuthor(Integer id, Author authorDetails);

    void deleteAuthor(Integer id);
}
