package com.api.library.controller.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "bookServiceImplV1")
    private BookService bookService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void BookController_CreateBook_ReturnCreated() throws JsonProcessingException, Exception {

        BookRequest request = BookRequest.builder()
                .title("The Great Gatsby")
                .isbn("978-3-16-148410-0")
                .aisleNumber(1)
                .authorId(1)
                .build();
        BookResponse response = BookResponse.builder()
                .id(1)
                .title("The Great Gatsby")
                .isbn("978-3-16-148410-0")
                .aisleNumber(1)
                .author(null)
                .build();
        given(bookService.addBook(any(BookRequest.class), eq(1))).willReturn(response);

        ResultActions result = mockMvc.perform(
                post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(request.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(request.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aisle_number").value(request.getAisleNumber()));

    }
}
