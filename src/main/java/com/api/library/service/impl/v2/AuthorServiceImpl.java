package com.api.library.service.impl.v2;

import static com.api.library.repository.AuthorSpecs.filterBy;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.dto.Pagination;
import com.api.library.dto.ResponseWrapper;
import com.api.library.exception.ResourceNotFoundException;
import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.v2.AuthorService;

import lombok.AllArgsConstructor;

@Service("authorServiceImplV2")
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    private ModelMapper modelMapper;

    @Override
    public ResponseWrapper<AuthorResponse> addAuthor(AuthorRequest authorRequest) {
        Author author =  authorRepository.save(modelMapper.map(authorRequest, Author.class));
        return ResponseWrapper.of("Author added successfully", "success", modelMapper.map(author, AuthorResponse.class));
    }

    @Override
    public ResponseWrapper<List<AuthorResponse>> getAllAuthors(AuthorRequest.AuthorRequestBuilder filterBuilder, Integer page, Integer size, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
               ? Sort.by(sortBy).ascending()
               : Sort.by(sortBy).descending();
       Pageable pageable = PageRequest.of(page, size, sortByAndOrder);

        AuthorRequest filter = filterBuilder.build();

        Specification<Author> spec = filterBy(filter.getFirstName(), filter.getLastName());

        Page<Author> authorPage = authorRepository.findAll(spec, pageable);

        Pagination pagination = new Pagination();
        pagination.setCurrentPage(authorPage.getPageable().getPageNumber());
        pagination.setPageSize(authorPage.getSize());
        pagination.setTotalElements(authorPage.getTotalElements());
        pagination.setTotalPages(authorPage.getTotalPages());
        pagination.setLastPage(authorPage.isLast());

        return ResponseWrapper.of("Authors retrieved successfully", "success", authorPage
                .map(author -> modelMapper.map(author, AuthorResponse.class))
                .getContent(), pagination);
    }

    @Override
    public ResponseWrapper<AuthorResponse> getAuthorById(Integer id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        return ResponseWrapper.of("Author retrieved successfully", "success", modelMapper.map(author, AuthorResponse.class));
    }

    @Override
    public ResponseWrapper<AuthorResponse> updateAuthor(Integer id, AuthorRequest authorRequest) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        author.setFirstName(authorRequest.getFirstName());
        author.setLastName(authorRequest.getLastName());
        return ResponseWrapper.of("Author updated successfully", "success", modelMapper.map(authorRepository.save(author), AuthorResponse.class));
    }

    @Override
    public ResponseWrapper<AuthorResponse> deleteAuthor(Integer id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        authorRepository.delete(author);
        return ResponseWrapper.of("Author deleted successfully", "success", modelMapper.map(author, AuthorResponse.class));
    }

}
