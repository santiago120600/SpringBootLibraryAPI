package com.api.library.service.impl.v2;

import static com.api.library.repository.AuthorSpecs.filterBy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.v2.AuthorService;

import lombok.AllArgsConstructor;

@Service("authorServiceImplV2")
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    @Override
    public Author addAuthor(Author authorRequest) {
        return authorRepository.save(authorRequest);
    }

    @Override
    public Page<Author> getAllAuthors(Pageable pageable, Author.AuthorBuilder filterBuilder) {
        Author filter = filterBuilder.build();

        Specification<Author> spec = filterBy(filter.getFirstName(), filter.getLastName());

        return authorRepository.findAll(spec, pageable);
    }

    @Override
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
            return authorRepository.save(author);
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
