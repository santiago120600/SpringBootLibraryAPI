package com.api.library.service.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.library.model.Author;

public interface AuthorService {

    Author addAuthor(Author author);

    Page<Author> getAllAuthors(Pageable pageable, Author.AuthorBuilder filterBuilder);

    Author getAuthorById(Integer id);

    Author updateAuthor(Integer id, Author authorDetails);

    void deleteAuthor(Integer id);
}
