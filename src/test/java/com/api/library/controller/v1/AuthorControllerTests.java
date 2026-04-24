package com.api.library.controller.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
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

import com.api.library.dto.AuthorRequest;
import com.api.library.dto.AuthorResponse;
import com.api.library.model.Author;
import com.api.library.service.v1.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = com.api.library.controller.v1.AuthorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthorControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean(name = "authorServiceImplV1")
        private AuthorService authorService;

        private final Integer AUTHOR_ID = 1;
        private final String FIRST_NAME = "John";
        private final String LAST_NAME = "Doe";
        private AuthorRequest authorRequest;
        private AuthorResponse authorResponse;
        private Author author;
        private ObjectMapper objectMapper;

        private AuthorRequest createAuthorRequest(String firstName, String lastName) {
                return AuthorRequest.builder()
                                .firstName(firstName)
                                .lastName(lastName)
                                .build();
        }

        private AuthorResponse createAuthorResponse(Integer id, String firstName, String lastName) {
                return AuthorResponse.builder()
                                .id(id)
                                .firstName(firstName)
                                .lastName(lastName)
                                .build();
        }

        private Author createAuthor(Integer id, String firstName, String lastName) {
                return Author.builder()
                                .id(id)
                                .firstName(firstName)
                                .lastName(lastName)
                                .build();
        }

        @BeforeEach
        public void setUp() {
                objectMapper = new ObjectMapper();
                authorRequest = createAuthorRequest(FIRST_NAME, LAST_NAME);
                authorResponse = createAuthorResponse(AUTHOR_ID, FIRST_NAME, LAST_NAME);
                author = createAuthor(AUTHOR_ID, FIRST_NAME, LAST_NAME);
        }

        @Test
        public void AuthorController_CreateAuthor_ReturnCreated() throws Exception {
                given(authorService.addAuthor(any(AuthorRequest.class))).willReturn(authorResponse);

                ResultActions result = mockMvc.perform(
                                post("/api/v1/authors")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(authorRequest)));

                result.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name",
                                                CoreMatchers.is(author.getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name",
                                                CoreMatchers.is(author.getLastName())));
        }

        @Test
        public void AuthorController_UpdateAuthor_ReturnOk() throws Exception {
                given(authorService.updateAuthor(any(Integer.class), any(AuthorRequest.class)))
                                .willReturn(authorResponse);

                ResultActions result = mockMvc.perform(
                                put("/api/v1/authors/{id}", AUTHOR_ID)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(authorRequest)));

                result.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name",
                                                CoreMatchers.is(author.getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name",
                                                CoreMatchers.is(author.getLastName())));
        }

        @Test
        public void AuthorController_DeleteAuthor_ReturnNoContent() throws Exception {
                doNothing().when(authorService).deleteAuthor(AUTHOR_ID);
                ResultActions result = mockMvc.perform(
                                delete("/api/v1/authors/{id}", AUTHOR_ID)
                                                .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(MockMvcResultMatchers.status().isNoContent());
        }

        @Test
        public void AuthorController_GetAuthorById_ReturnOk() throws Exception {
                given(authorService.getAuthorById(any(Integer.class))).willReturn(authorResponse);

                ResultActions result = mockMvc.perform(
                                get("/api/v1/authors/{id}", AUTHOR_ID)
                                                .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name",
                                                CoreMatchers.is(author.getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name",
                                                CoreMatchers.is(author.getLastName())));
        }

        @Test
        public void AuthorController_GetAuthors_ReturnOk() throws Exception {
                List<AuthorResponse> mockAuthors = Arrays.asList(
                                AuthorResponse.builder()
                                                .id(1)
                                                .firstName("John")
                                                .lastName("Doe")
                                                .build(),
                                AuthorResponse.builder()
                                                .id(2)
                                                .firstName("Jane")
                                                .lastName("Smith")
                                                .build());

                given(authorService.getAllAuthors(any(AuthorRequest.AuthorRequestBuilder.class)))
                                .willReturn(mockAuthors);

                ResultActions result = mockMvc.perform(
                                get("/api/v1/authors")
                                                .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(mockAuthors.get(0).getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].first_name",
                                                CoreMatchers.is(mockAuthors.get(0).getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].last_name",
                                                CoreMatchers.is(mockAuthors.get(0).getLastName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(mockAuthors.get(1).getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].first_name",
                                                CoreMatchers.is(mockAuthors.get(1).getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].last_name",
                                                CoreMatchers.is(mockAuthors.get(1).getLastName())));
        }

}
