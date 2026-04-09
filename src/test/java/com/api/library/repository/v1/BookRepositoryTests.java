package com.api.library.repository.v1;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void BookRepository_FindById_ReturnsBook() {
        Author author = Author.builder()
        .firstName("Juan")
        .lastName("Balderas")
        .build();

        authorRepository.save(author);

        Book book = Book.builder()
        .title("The Lord of the Rings")
        .isbn("978-0544003415")
        .aisleNumber(1)
        .author(author)
        .build();

        bookRepository.save(book);
        Book foundBook = bookRepository.findById(book.getId()).get();
        Assertions.assertThat(foundBook).isNotNull();}
}
