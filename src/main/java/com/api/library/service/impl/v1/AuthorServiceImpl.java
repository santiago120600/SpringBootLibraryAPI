package com.api.library.service.impl.v1;

import static com.api.library.repository.AuthorSpecs.filterBy;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.exception.ResourceNotFoundException;
import com.api.library.model.Author;
import com.api.library.repository.AuthorRepository;
import com.api.library.service.v1.AuthorService;

import lombok.AllArgsConstructor;

@Service("authorServiceImplV1")
@Transactional
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    private ModelMapper modelMapper;

    @Override
    public AuthorResponse addAuthor(AuthorRequest authorRequest) {
        Author savedAuthor = authorRepository.save(modelMapper.map(authorRequest, Author.class));
        return modelMapper.map(savedAuthor, AuthorResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> getAllAuthors(AuthorRequest.AuthorRequestBuilder filterBuilder) {
        AuthorRequest filter = filterBuilder.build();

        Specification<Author> spec = filterBy(filter.getFirstName(), filter.getLastName());
        List<Author> authorResponses = authorRepository.findAll(spec);
        return authorResponses.stream()
                .map(author -> modelMapper.map(author, AuthorResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getAuthorById(Integer id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        return modelMapper.map(author, AuthorResponse.class);
    }

    @Override
    public AuthorResponse updateAuthor(Integer id, AuthorRequest authorRequest) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        author.setFirstName(authorRequest.getFirstName());
        author.setLastName(authorRequest.getLastName());
        return modelMapper.map(authorRepository.save(author), AuthorResponse.class);
    }

    @Override
    public void deleteAuthor(Integer id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        authorRepository.delete(author);
    }
}
