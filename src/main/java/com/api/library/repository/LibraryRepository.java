package com.api.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.library.controller.Library;

public interface LibraryRepository extends JpaRepository<Library, String>{

}
