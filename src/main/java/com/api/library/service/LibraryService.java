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

    public String buildId(String isbn, int aisle){
       return isbn + Integer.toString(aisle); 
    }

    public boolean checkBookAlreadyExists(String id){
        Optional<Book> book = repository.findById(id);
        return book.isPresent();
    }
}
