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
import com.api.library.repository.AuthorRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthorRepositoryTests {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void AuthorRepository_FindById_ReturnsAuthor() {
        Author author = Author.builder()
        .firstName("Juan")
        .lastName("Balderas")
        .build();

        authorRepository.save(author);
        Author foundAuthor = authorRepository.findById(author.getId()).get();
        Assertions.assertThat(foundAuthor).isNotNull();
    }


    
}
