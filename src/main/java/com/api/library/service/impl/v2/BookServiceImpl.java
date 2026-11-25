package com.api.library.service.impl.v2;

import static com.api.library.repository.BookSpecs.filterBy;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.dto.Pagination;
import com.api.library.dto.ResponseWrapper;
import com.api.library.exception.ResourceNotFoundException;
import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;
import com.api.library.service.v2.BookService;

import lombok.AllArgsConstructor;

@Service("bookServiceImplV2")
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    private AuthorRepository authorRepository;

    private ModelMapper modelMapper;

    @Override
    public ResponseWrapper<BookResponse> addBook(BookRequest bookRequest, Integer authorId) {
        Book existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
        if (existingBook == null) {
            Book book = modelMapper.map(bookRequest, Book.class);
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Author", "id", authorId));
            book.setAuthor(author);
            return ResponseWrapper.of("Book added successfully", "success", modelMapper.map(bookRepository.save(book), BookResponse.class));
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book already exists");
        }
    }

    @Override
    public ResponseWrapper<List<BookResponse>> getAllBooks(BookRequest.BookRequestBuilder filterBuilder, Integer page,
            Integer size, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sortByAndOrder);

        BookRequest filter = filterBuilder.build();

        Specification<Book> spec = filterBy(filter.getAisleNumber(), filter.getIsbn(), filter.getTitle());

        Page<Book> bookPage = bookRepository.findAll(spec, pageable);

        Pagination pagination = new Pagination();
        pagination.setCurrentPage(bookPage.getPageable().getPageNumber());
        pagination.setPageSize(bookPage.getSize());
        pagination.setTotalElements(bookPage.getTotalElements());
        pagination.setTotalPages(bookPage.getTotalPages());
        pagination.setLastPage(bookPage.isLast());

        return ResponseWrapper.of("Books retrieved successfully", "success", bookPage
                .map(item -> modelMapper.map(item, BookResponse.class))
                .getContent(), pagination);
    }

    @Override
    public ResponseWrapper<BookResponse> getBookById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return ResponseWrapper.of("Book retrieved successfully", "success", modelMapper.map(bookRepository.save(book), BookResponse.class));
    }

    @Override
    public ResponseWrapper<BookResponse> updateBook(Integer id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        book.setAisleNumber(bookRequest.getAisleNumber());
        book.setIsbn(bookRequest.getIsbn());
        book.setTitle(bookRequest.getTitle());
        Author author = authorRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
        ;
        book.setAuthor(author);
        return ResponseWrapper.of("Book updated successfully", "success", modelMapper.map(bookRepository.save(book), BookResponse.class));
    }

    @Override
    public ResponseWrapper<BookResponse> deleteBook(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        bookRepository.delete(book);
        return ResponseWrapper.of("Book deleted successfully", "success", modelMapper.map(book, BookResponse.class));
    }

}
