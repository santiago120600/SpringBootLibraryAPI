package com.api.library.repository.v1;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthorRepositoryTests {

    private final Integer AUTHOR_ID = 1;
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private Author author;

    @Autowired
    private AuthorRepository authorRepository;

    private Author createAuthor(Integer id, String firstName, String lastName) {
        return Author.builder()
        .firstName(firstName)
        .lastName(lastName)
        .build();
    }

    @BeforeEach
    public void setUp(){
        author = createAuthor(AUTHOR_ID, FIRST_NAME, LAST_NAME);
    }

    @Test
    public void AuthorRepository_FindById_ReturnsAuthor() {
        authorRepository.save(author);
        Author foundAuthor = authorRepository.findById(author.getId()).get();
        Assertions.assertThat(foundAuthor).isNotNull();
        Assertions.assertThat(foundAuthor.getId()).isEqualTo(author.getId());
        Assertions.assertThat(foundAuthor.getFirstName()).isEqualTo(author.getFirstName());
        Assertions.assertThat(foundAuthor.getLastName()).isEqualTo(author.getLastName());
    }

    @Test
    public void AuthorRepository_FindAll_ReturnsAuthors() {
        authorRepository.save(author);
        Author anotherAuthor = createAuthor(2, "Jane", "Smith");
        authorRepository.save(anotherAuthor);
        List<Author> authors = authorRepository.findAll();
        Assertions.assertThat(authors).isNotNull();
        Assertions.assertThat(authors).hasSize(2);
    }

    @Test
    public void AuthorRepository_Save_ReturnsSavedAuthor() {
        Author savedAuthor = authorRepository.save(author);
        Assertions.assertThat(savedAuthor).isNotNull();
        Assertions.assertThat(savedAuthor.getId()).isNotNull();
        Assertions.assertThat(savedAuthor.getFirstName()).isEqualTo(author.getFirstName());
        Assertions.assertThat(savedAuthor.getLastName()).isEqualTo(author.getLastName());
    }

    @Test
    public void AuthorRepository_Update_ReturnsUpdatedAuthor() {
        String updatedFirstName = "Jane";
        String updatedLastName = "Smith";
        Author savedAuthor = authorRepository.save(author);
        savedAuthor.setFirstName(updatedFirstName);
        savedAuthor.setLastName(updatedLastName);
        Author updatedAuthor = authorRepository.save(savedAuthor);
        Assertions.assertThat(updatedAuthor).isNotNull();
        Assertions.assertThat(updatedAuthor.getId()).isEqualTo(savedAuthor.getId());
        Assertions.assertThat(updatedAuthor.getFirstName()).isEqualTo(updatedFirstName);
        Assertions.assertThat(updatedAuthor.getLastName()).isEqualTo(updatedLastName);
    }

    @Test
    public void AuthorRepository_Delete_RemovesAuthor() {
        Author savedAuthor = authorRepository.save(author);
        authorRepository.delete(savedAuthor);
        Assertions.assertThat(authorRepository.findById(savedAuthor.getId())).isEmpty();
    }
    
}
