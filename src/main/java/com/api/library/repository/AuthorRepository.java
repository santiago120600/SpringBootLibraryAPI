package com.api.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.api.library.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer>,JpaSpecificationExecutor<Author>{
}
