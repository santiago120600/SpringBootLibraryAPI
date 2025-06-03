package com.api.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.api.library.controller.Library;
import com.api.library.repository.LibraryRepository;


@SpringBootApplication
public class LibraryApplication implements CommandLineRunner{

    @Autowired
    LibraryRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

    @Override
    public void run(String[] args){
        Library lib = new Library();
        lib.setBook_name("viaje submarino");
        lib.setId("isbn12");
        lib.setIsbn("87489327349287");
        lib.setAisle(2);
        lib.setAuthor("Juan");
        repository.save(lib);
    }

}
