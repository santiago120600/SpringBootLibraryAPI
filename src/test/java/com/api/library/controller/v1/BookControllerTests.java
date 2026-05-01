package com.api.library.controller.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.api.library.dto.AuthorResponse;
import com.api.library.dto.BookRequest;
import com.api.library.dto.BookResponse;
import com.api.library.service.v1.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = com.api.library.controller.v1.BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BookControllerTests {

        private BookRequest bookRequest;
        private BookResponse bookResponse;
        private AuthorResponse authorResponse;

        private final Integer BOOK_ID = 1;
        private final String TITLE = "The Great Gatsby";
        private final String ISBN = "978-3-16-148410-0";
        private final Integer AISLE_NUMBER = 1;
        private final Integer AUTHOR_ID = 1;
        private final String FIRST_NAME = "John";
        private final String LAST_NAME = "Doe";

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean(name = "bookServiceImplV1")
        private BookService bookService;

        private ObjectMapper objectMapper;

        @BeforeEach
        public void setUp() {
                objectMapper = new ObjectMapper();
                bookRequest = createBookRequest(TITLE, ISBN, AISLE_NUMBER, AUTHOR_ID);
                authorResponse = createAuthorResponse(AUTHOR_ID, FIRST_NAME, LAST_NAME);
                bookResponse = createBookResponse(BOOK_ID, TITLE, ISBN, AISLE_NUMBER, authorResponse);
        }

        private BookRequest createBookRequest(String title, String isbn, Integer aisleNumber, Integer authorId) {
                return BookRequest.builder()
                                .title(title)
                                .isbn(isbn)
                                .aisleNumber(aisleNumber)
                                .authorId(authorId)
                                .build();
        }

        private AuthorResponse createAuthorResponse(Integer id, String firstName, String lastName) {
                return AuthorResponse.builder()
                                .id(id)
                                .firstName(firstName)
                                .lastName(lastName)
                                .build();
        }

        private BookResponse createBookResponse(Integer id, String title, String isbn, Integer aisleNumber,
                        AuthorResponse author) {
                return BookResponse.builder()
                                .id(id)
                                .title(title)
                                .isbn(isbn)
                                .aisleNumber(aisleNumber)
                                .author(author)
                                .build();
        }

        @Test
        public void BookController_CreateBook_ReturnCreated() throws JsonProcessingException, Exception {
                given(bookService.addBook(any(BookRequest.class), eq(AUTHOR_ID))).willReturn(bookResponse);

                ResultActions result = mockMvc.perform(
                                post("/api/v1/books")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(bookRequest)));

                result.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BOOK_ID))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookRequest.getTitle()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookRequest.getIsbn()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.aisle_number")
                                                .value(bookRequest.getAisleNumber()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.id").value(AUTHOR_ID))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.first_name")
                                                .value(authorResponse.getFirstName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.last_name")
                                                .value(authorResponse.getLastName()));
        }

        @Test
        public void BookController_UpdateBook_ReturnOk() throws JsonProcessingException, Exception {
                given(bookService.updateBook(eq(BOOK_ID), any(BookRequest.class)))
                                .willReturn(bookResponse);

                ResultActions result = mockMvc.perform(
                                put("/api/v1/books/{id}", BOOK_ID)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(bookRequest)));

                result.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BOOK_ID))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookRequest.getTitle()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookRequest.getIsbn()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.aisle_number")
                                                .value(bookRequest.getAisleNumber()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.id").value(AUTHOR_ID))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.first_name")
                                                .value(authorResponse.getFirstName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.last_name")
                                                .value(authorResponse.getLastName()));
        }
        
        @Test
        public void BookController_GetBooks_ReturnOk() throws Exception {
                given(bookService.getAllBooks(any(BookRequest.BookRequestBuilder.class)))
                                .willReturn(List.of(bookResponse));

                ResultActions result = mockMvc.perform(
                                get("/api/v1/books")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .param("title", TITLE)
                                                .param("isbn", ISBN)
                                                .param("aisleNumber", String.valueOf(AISLE_NUMBER)));

                result.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(BOOK_ID))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(bookResponse.getTitle()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value(bookResponse.getIsbn()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].aisle_number")
                                                .value(bookResponse.getAisleNumber()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author.id").value(AUTHOR_ID))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author.first_name")
                                                .value(authorResponse.getFirstName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author.last_name")
                                                .value(authorResponse.getLastName()));
        }
        
        @Test
        public void BookController_GetBookById_ReturnOk() throws Exception {
                given(bookService.getBookById(eq(BOOK_ID))).willReturn(bookResponse);

                ResultActions result = mockMvc.perform(
                                get("/api/v1/books/{id}", BOOK_ID)
                                                .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BOOK_ID))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookResponse.getTitle()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(bookResponse.getIsbn()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.aisle_number")
                                                .value(bookResponse.getAisleNumber()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.id").value(AUTHOR_ID))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.first_name")
                                                .value(authorResponse.getFirstName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author.last_name")
                                                .value(authorResponse.getLastName()));
        }

        @Test
        public void BookController_DeleteBook_ReturnNoContent() throws Exception {
                doNothing().when(bookService).deleteBook(eq(BOOK_ID));
                ResultActions result = mockMvc.perform(
                                delete("/api/v1/books/{id}", BOOK_ID)
                                                .contentType(MediaType.APPLICATION_JSON));
                result.andExpect(MockMvcResultMatchers.status().isNoContent());
        }

}
