package com.api.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.library.repository.LibraryRepository;
import java.util.Optional;
import com.api.library.controller.Library;

@Service
public class LibraryService{

    @Autowired
    LibraryRepository repository;

    public String buildId(String isbn, int aisle){
       return isbn + Integer.toString(aisle); 
    }

    public boolean checkBookAlreadyExists(String id){
        Optional<Library> lib = repository.findById(id);
        return lib.isPresent();
    }
}
