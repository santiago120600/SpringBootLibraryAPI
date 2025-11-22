package com.api.library.service.impl.v2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import static com.api.library.repository.AuthorSpecs.filterBy;

@Service("authorServiceImplV2")
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public ResponseWrapper addAuthor(AuthorRequest authorRequest) {
        Author savedAuthor = authorRepository.save(mapToEntity(authorRequest));
        return wrap("Author added successfully", "success",mapToDTO(savedAuthor));
    }

    @Override
    public ResponseWrapper getAllAuthors(Pageable pageable, AuthorRequest.AuthorRequestBuilder filterBuilder) {
        AuthorRequest filter = filterBuilder.build();

        Specification<Author> spec = filterBy(filter.getFirstName(), filter.getLastName());

        Page<Author> authorPage = authorRepository.findAll(spec, pageable);

        List<AuthorResponse> authorResponses = authorPage.getContent().stream().map(author -> {
            return mapToDTO(author);
        }).collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .currentPage(authorPage.getNumber())
                .pageSize(authorPage.getSize())
                .totalElements(authorPage.getTotalElements())
                .totalPages(authorPage.getTotalPages())
                .build();
        return wrap("Authors retrieved successfully", "success", authorResponses, pagination);
    }

    @Override
    public ResponseWrapper getAuthorById(Integer id) {
         try{
            Author author = authorRepository.findById(id).get();
            return wrap("Author retrieved successfully", "success", mapToDTO(author));
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
            return wrap("Author updated successfully", "success", mapToDTO(author));
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
            return wrap("Author deleted successfully", "success", response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private AuthorResponse mapToDTO(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .build();
    }

    private Author mapToEntity(AuthorRequest authorRequest) {
        return Author.builder()
                .firstName(authorRequest.getFirstName())
                .lastName(authorRequest.getLastName())
                .build();
    }

    private ResponseWrapper wrap(String msg, String status, Object data) {
        return wrap(msg, status, data, null);
    }

    private ResponseWrapper wrap(String msg, String status,Object data, Pagination p) {
        return ResponseWrapper.builder()
                .message(msg)
                .status(status)
                .data(data)
                .pagination(p)
                .build();
    }
}
