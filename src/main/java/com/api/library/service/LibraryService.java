package com.api.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.library.repository.LibraryRepository;
import java.util.Optional;
import com.api.library.model.Book;

@Service
public class LibraryService{

    @Autowired
    LibraryRepository repository;

}
