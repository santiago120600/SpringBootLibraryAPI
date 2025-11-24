package com.api.library.service.impl.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.v1.AuthorService;

@Service("authorServiceImplV1")
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public AuthorResponse addAuthor(AuthorRequest authorRequest) {
        Author savedAuthor = authorRepository.save(mapToEntity(authorRequest));
        return mapToDTO(savedAuthor);
    }

    @Override
    public List<AuthorResponse> getAllAuthors() {
        List<AuthorResponse> authorResponses = authorRepository.findAll().stream().map(author -> {
            return mapToDTO(author);
        }).collect(Collectors.toList());
        return authorResponses;
    }

    @Override
    public AuthorResponse getAuthorById(Integer id) {
         try{
            Author author = authorRepository.findById(id).get();
            return mapToDTO(author);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public AuthorResponse updateAuthor(Integer id, AuthorRequest authorRequest) {
         try{
            Author author = authorRepository.findById(id).get();
            author.setFirstName(authorRequest.getFirstName());
            author.setLastName(authorRequest.getLastName());
            authorRepository.save(author);
            return mapToDTO(author);
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

    private AuthorResponse mapToDTO(Author author) {
        AuthorResponse response = new AuthorResponse();
        response.setId(author.getId());
        response.setFirstName(author.getFirstName());
        response.setLastName(author.getLastName());
        return response;
    }

    private Author mapToEntity(AuthorRequest authorRequest) {
        Author author = new Author();
        author.setFirstName(authorRequest.getFirstName());
        author.setLastName(authorRequest.getLastName());
        return author;
    }
}
