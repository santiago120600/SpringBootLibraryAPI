package com.api.library.repository;

import java.util.List;

import com.api.library.controller.Library;

public interface LibraryRepositoryCustom {

    List<Library> findByAuthor(String author);
}
