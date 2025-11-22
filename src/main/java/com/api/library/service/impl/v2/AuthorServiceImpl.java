package com.api.library.service.impl.v2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.dto.Pagination;
import com.api.library.dto.ResponseWrapper;
import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.v2.AuthorService;

@Service("authorServiceImplV2")
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public ResponseWrapper addAuthor(AuthorRequest authorRequest) {
        Author savedAuthor = authorRepository.save(mapToEntity(authorRequest));
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Author added successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(mapToDTO(savedAuthor));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper getAllAuthors(Pageable pageable) {
        Page<Author> authorPage = authorRepository.findAll(pageable);

        List<AuthorResponse> authorResponses = authorPage.getContent().stream().map(author -> {
            return mapToDTO(author);
        }).collect(Collectors.toList());

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Authors retrieved successfully");
        responseWrapper.setStatus("success");
        responseWrapper.setData(authorResponses);
        Pagination pagination = new Pagination();
        pagination.setCurrentPage(authorPage.getNumber());
        pagination.setPageSize(authorPage.getSize());
        pagination.setTotalElements(authorPage.getTotalElements());
        pagination.setTotalPages(authorPage.getTotalPages());
        responseWrapper.setPagination(pagination);
        return responseWrapper;
    }

    @Override
    public ResponseWrapper getAuthorById(Integer id) {
         try{
            Author author = authorRepository.findById(id).get();
            ResponseWrapper responseWrapper = new ResponseWrapper();
            responseWrapper.setMessage("Author retrieved successfully");
            responseWrapper.setStatus("success");
            responseWrapper.setData(mapToDTO(author));
            return responseWrapper;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseWrapper updateAuthor(Integer id, AuthorRequest authorRequest) {
         try{
            Author author = authorRepository.findById(id).get();
            author.setFirstName(authorRequest.getFirstName());
            author.setLastName(authorRequest.getLastName());
            authorRepository.save(author);
            ResponseWrapper responseWrapper = new ResponseWrapper();
            responseWrapper.setData(mapToDTO(author));
            responseWrapper.setStatus("success");
            responseWrapper.setMessage("Author updated successfully");
            return responseWrapper;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseWrapper deleteAuthor(Integer id) {
        try {
            Author author = authorRepository.findById(id).get();
            authorRepository.delete(author);
            AuthorResponse response = new AuthorResponse();
            ResponseWrapper responseWrapper = new ResponseWrapper();
            responseWrapper.setStatus("success");
            responseWrapper.setData(response);
            responseWrapper.setMessage("Author deleted successfully");
            return responseWrapper;
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
