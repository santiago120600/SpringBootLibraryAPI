package com.api.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.library.model.Book;
import java.util.List;

public interface LibraryRepository extends JpaRepository<Book, String> {
    List<Book> findByAuthor(String author);
}
