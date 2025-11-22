package com.api.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.api.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b WHERE b.author.firstName = ?1")
    List<Book> findByAuthorName(String authorName);
    Book findByIsbn(String isbn);
}
