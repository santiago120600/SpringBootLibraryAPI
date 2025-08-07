package com.api.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.model.Author;
import com.api.library.model.Book;
import com.api.library.repository.AuthorRepository;
import com.api.library.repository.BookRepository;

@RestController
public class LibraryController{

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @PostMapping("/book") 
    public ResponseEntity<BookResponse> addBook(@RequestBody BookRequest bookRequest){
        HttpHeaders headers = new HttpHeaders();
        BookResponse response = new BookResponse();
        Book existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
        if(existingBook == null){
            logger.info("Book does not exists so creating one");
            Book book = new Book();
            book.setIsbn(bookRequest.getIsbn());
            book.setTitle(bookRequest.getTitle());
            book.setAisleNumber(bookRequest.getAisleNumber());
            Author author = authorRepository.findById(bookRequest.getAuthorId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));;
            book.setAuthor(author);
            Book savedBook = bookRepository.save(book);
            response.setId(savedBook.getId());
            response.setIsbn(savedBook.getIsbn());
            response.setAisleNumber(savedBook.getAisleNumber());
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(author.getId());
            authorResponse.setFirstName(author.getFirstName());
            authorResponse.setLastName(author.getLastName());
            response.setAuthor(authorResponse);
            response.setMessage("Book added successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }else{
            response.setAisleNumber(existingBook.getAisleNumber());
            response.setIsbn(existingBook.getIsbn());
            response.setId(existingBook.getId());
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(existingBook.getAuthor().getId());
            authorResponse.setFirstName(existingBook.getAuthor().getFirstName());
            authorResponse.setLastName(existingBook.getAuthor().getLastName());
            response.setAuthor(authorResponse);
            response.setMessage("Book already exists");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping(path = "/book")
    public ResponseEntity<List<Book>> getBooks(){
        List<Book> books = bookRepository.findAll();
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable(value = "id")Integer id){
        try{
            Book book = bookRepository.findById(id).get();
            BookResponse response = new BookResponse();
            response.setMessage("Book retrieved successfully");
            response.setId(book.getId());
            response.setTitle(book.getTitle());
            response.setIsbn(book.getIsbn());
            response.setAisleNumber(book.getAisleNumber());
            AuthorResponse author = new AuthorResponse();
            author.setId(book.getAuthor().getId());
            author.setFirstName(book.getAuthor().getFirstName());
            author.setLastName(book.getAuthor().getLastName());
            response.setAuthor(author);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/book", params = "authorName")
    public ResponseEntity<List<Book>> getBookByAuthor(@RequestParam(value = "authorName")String authorName){
        try{
           List<Book> books = bookRepository.findByAuthorName(authorName); 
           return new ResponseEntity<>(books, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable(value = "id")Integer id, @RequestBody BookRequest bookRequest){
        try{
            Book book = bookRepository.findById(id).get();
            book.setAisleNumber(bookRequest.getAisleNumber());
            book.setIsbn(bookRequest.getIsbn());
            book.setTitle(bookRequest.getTitle());
            Author author = authorRepository.findById(bookRequest.getAuthorId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));;
            book.setAuthor(author);
            bookRepository.save(book);
            BookResponse response = new BookResponse();
            response.setMessage("Book updated successfully");
            response.setId(book.getId());
            response.setTitle(book.getTitle());
            response.setIsbn(book.getIsbn());
            response.setAisleNumber(book.getAisleNumber());
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(author.getId());
            authorResponse.setFirstName(author.getFirstName());
            authorResponse.setLastName(author.getLastName());
            response.setAuthor(authorResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<BookResponse> deleteBook(@PathVariable(value = "id")Integer id){
        try{
            Book book = bookRepository.findById(id).get();
            bookRepository.delete(book);
            BookResponse response = new BookResponse();
            response.setMessage("Book deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/author")
    public ResponseEntity<List<Author>> getAuthors(){
        List<Author> authors = authorRepository.findAll();
        return new ResponseEntity<>(authors,HttpStatus.OK);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable(value = "id")Integer id){
        try{
            Author author = authorRepository.findById(id).get();
            AuthorResponse authorResponse = new AuthorResponse();
            authorResponse.setId(author.getId());
            authorResponse.setFirstName(author.getFirstName());
            authorResponse.setLastName(author.getLastName());
            authorResponse.setMessage("Author retrieved successfully");
            return new ResponseEntity<>(authorResponse, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/author") 
    public ResponseEntity<AuthorResponse> addAuthor(@RequestBody AuthorRequest authorRequest){
        AuthorResponse response = new AuthorResponse();
        Author author = new Author();
        author.setFirstName(authorRequest.getFirstName());
        author.setLastName(authorRequest.getLastName());
        Author savedAuthor = authorRepository.save(author);
        response.setId(savedAuthor.getId());
        response.setFirstName(savedAuthor.getFirstName());
        response.setLastName(savedAuthor.getLastName());
        response.setMessage("Author added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/author/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable(value = "id")Integer id, @RequestBody AuthorRequest authorRequest){
        try{
            Author author = authorRepository.findById(id).get();
            author.setFirstName(authorRequest.getFirstName());
            author.setLastName(authorRequest.getLastName());
            authorRepository.save(author);
            AuthorResponse response = new AuthorResponse();
            response.setId(author.getId());
            response.setFirstName(author.getFirstName());
            response.setLastName(author.getLastName());
            response.setMessage("Author updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<AuthorResponse> deleteAuthor(@PathVariable(value = "id")Integer id){
        try{
            Author author = authorRepository.findById(id).get();
            authorRepository.delete(author);
            AuthorResponse response = new AuthorResponse();
            response.setMessage("Author deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    
}
