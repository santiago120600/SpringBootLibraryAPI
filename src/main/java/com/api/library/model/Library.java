package com.api.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Book")
@Getter
@Setter
public class Library {

    @Column(name="book_name")
    private String book_name;

    @Id
    @Column(name="id")
    private String id;

    @Column(name="isbn")
    private String isbn;

    @Column(name="aisle")
    private int aisle;

    @Column(name="author")
    private String author;
}
