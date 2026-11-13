package com.api.library.service.impl.v1;

import static com.api.library.repository.AuthorSpecs.filterBy;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.v1.AuthorService;

@Service("authorServiceImplV1")
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
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
         try{
            return authorRepository.findById(id).get();
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Author updateAuthor(Integer id, Author authorRequest) {
         try{
            Author author = authorRepository.findById(id).get();
            author.setFirstName(authorRequest.getFirstName());
            author.setLastName(authorRequest.getLastName());
            authorRepository.save(author);
            return author;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteAuthor(Integer id) {
        try {
            Author author = authorRepository.findById(id).get();
            authorRepository.delete(author);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
