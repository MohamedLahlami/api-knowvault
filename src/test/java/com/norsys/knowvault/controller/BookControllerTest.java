package com.norsys.knowvault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.norsys.knowvault.config.TestSecurityConfig;
import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        testBookDTO = new BookDTO();
        testBookDTO.setId(1L);
        testBookDTO.setBookTitle("Test Book");
        testBookDTO.setUtilisateurLogin("testuser");
        testBookDTO.setDescription("Test book description");
        testBookDTO.setShelfId(1L);
        testBookDTO.setCreatedAt(LocalDateTime.now());
        testBookDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    void testFindAllBooks() throws Exception {
        // Given
        List<BookDTO> books = Arrays.asList(testBookDTO);
        Page<BookDTO> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), books.size());
        
        when(bookService.findAll(any(Pageable.class))).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/book")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].bookTitle").value("Test Book"));
    }

    @Test
    void testGetAllBooksPublic() throws Exception {
        // Given
        List<BookDTO> books = Arrays.asList(testBookDTO);
        when(bookService.findAllBooks()).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/book/public"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].bookTitle").value("Test Book"));
    }

    @Test
    @WithMockUser
    void testCreateBook() throws Exception {
        // Given
        when(bookService.create(any(BookDTO.class), any())).thenReturn(testBookDTO);

        // When & Then
        mockMvc.perform(post("/api/book")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookTitle").value("Test Book"));
    }

    @Test
    @WithMockUser
    void testGetBookById() throws Exception {
        // Given
        when(bookService.findById(1L)).thenReturn(testBookDTO);

        // When & Then
        mockMvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookTitle").value("Test Book"));
    }

    @Test
    @WithMockUser
    void testUpdateBook() throws Exception {
        // Given
        testBookDTO.setBookTitle("Updated Book Title");
        when(bookService.update(eq(1L), any(BookDTO.class))).thenReturn(testBookDTO);

        // When & Then
        mockMvc.perform(put("/api/book/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookTitle").value("Updated Book Title"));
    }
}
