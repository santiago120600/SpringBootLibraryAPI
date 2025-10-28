package com.api.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Column(name="title", nullable = false)
    private String title;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="isbn", unique = true, nullable = false)
    private String isbn;

    @Column(name="aisle_number")
    private Integer aisleNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author_id", nullable = false)
    private Author author;
}
