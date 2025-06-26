package com.api.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.library.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer>{
}
