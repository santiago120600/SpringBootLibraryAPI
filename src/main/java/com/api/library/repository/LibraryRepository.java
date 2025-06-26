package com.api.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.library.model.Book;

public interface LibraryRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE b.author.author_name = ?1")
    List<Book> findByAuthorName(String authorName);
    Book findByIsbn(String isbn);
}
