package com.api.library.service.impl.v2;

import static com.api.library.repository.AuthorSpecs.filterBy;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.AuthorResponse;
import com.api.library.dto.Pagination;
import com.api.library.dto.ResponseWrapper;
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
    public Author addAuthor(Author authorRequest) {
        return authorRepository.save(authorRequest);
    }

    @Override
    public ResponseWrapper getAllAuthors(Author.AuthorBuilder filterBuilder, Integer page, Integer size, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
               ? Sort.by(sortBy).ascending()
               : Sort.by(sortBy).descending();
       Pageable pageable = PageRequest.of(page, size, sortByAndOrder);

        Author filter = filterBuilder.build();

        Specification<Author> spec = filterBy(filter.getFirstName(), filter.getLastName());

        Page<Author> authorPage = authorRepository.findAll(spec, pageable);

        Pagination pagination = new Pagination();
        pagination.setCurrentPage(authorPage.getPageable().getPageNumber());
        pagination.setPageSize(authorPage.getSize());
        pagination.setTotalElements(authorPage.getTotalElements());
        pagination.setTotalPages(authorPage.getTotalPages());
        pagination.setLastPage(authorPage.isLast());

        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("Authors retrieved successfully");
        response.setStatus("success");
        response.setData(authorPage
                .map(author -> modelMapper.map(author, AuthorResponse.class))
                .getContent());
        response.setPagination(pagination);

        return response;
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
