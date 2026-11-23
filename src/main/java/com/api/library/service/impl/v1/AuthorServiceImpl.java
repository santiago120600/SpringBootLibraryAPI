package com.api.library.service.impl.v1;

import static com.api.library.repository.AuthorSpecs.filterBy;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.v1.AuthorService;

import lombok.AllArgsConstructor;

@Service("authorServiceImplV1")
@Transactional
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    @Override
    public Author addAuthor(Author authorRequest) {
        Author savedAuthor = authorRepository.save(authorRequest);
        return savedAuthor;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAllAuthors(Author.AuthorBuilder filterBuilder) {
        Author filter = filterBuilder.build();

        Specification<Author> spec = filterBy(filter.getFirstName(), filter.getLastName());
        List<Author> authorResponses = authorRepository.findAll(spec).stream().map(author -> {
            return author;
        }).collect(Collectors.toList());
        return authorResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorById(Integer id) {
        return authorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Not Found"));
    }

    @Override
    public Author updateAuthor(Integer id, Author authorRequest) {
        authorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Not Found"));
        authorRequest.setId(id);
        return authorRepository.save(authorRequest);
    }

    @Override
    public void deleteAuthor(Integer id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Not Found"));
        authorRepository.delete(author);
    }
}
