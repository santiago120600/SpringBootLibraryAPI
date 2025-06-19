package com.api.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.library.model.Library;
import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, String> {
    List<Library> findByAuthor(String author);
}
