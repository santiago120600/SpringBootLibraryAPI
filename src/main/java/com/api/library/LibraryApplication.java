package com.api.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.api.library.repository.LibraryRepository;


@SpringBootApplication
public class LibraryApplication{

    @Autowired
    LibraryRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}
