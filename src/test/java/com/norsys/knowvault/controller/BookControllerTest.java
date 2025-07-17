package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser
    public void testFindAllBooks() throws Exception {
        List<BookDTO> books = Arrays.asList(
                new BookDTO(1L, "Title1", UUID.fromString("32586d17-52c0-44a9-92ba-68759ddfd280"), 100L),
                new BookDTO(2L, "Title2", UUID.fromString("ad11bd76-b3ad-4ab9-acfb-556e3e648ab4"), 200L)
        );

        Mockito.when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Correct JSON path using bookTitle (your JSON uses bookTitle, not title)
                .andExpect(jsonPath("$[0].bookTitle").value("Title1"))
                .andExpect(jsonPath("$[1].bookTitle").value("Title2"));
    }

    @Test
    @WithMockUser
    public void testFindById() throws Exception {
        BookDTO book = new BookDTO(1L, "Title1", UUID.fromString("59cd73f6-6c5f-48a9-b014-e702bcceb824"), 100L);

        Mockito.when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookTitle").value("Title1"));
    }

    @Test
    @WithMockUser
    public void testCreateBook() throws Exception {
        BookDTO newBook = new BookDTO(null, "TitleNew", UUID.fromString("62b8037a-7b57-40ea-aa2d-5018506cb77a"), 100L);
        BookDTO createdBook = new BookDTO(1L, "TitleNew", UUID.fromString("0cd63130-287d-4b6e-bd3a-4ab60e756fc6"), 100L);

        Mockito.when(bookService.create(any(BookDTO.class))).thenReturn(createdBook);

        String jsonContent = "{\"id\":null,\"bookTitle\":\"TitleNew\",\"utilisateurId\":\"62b8037a-7b57-40ea-aa2d-5018506cb77a\",\"shelfId\":100}";

        mockMvc.perform(post("/api/book")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookTitle").value("TitleNew"));
    }

    @Test
    @WithMockUser
    public void testUpdateBook() throws Exception {
        BookDTO updateBook = new BookDTO(null, "TitleUpdated", UUID.fromString("eb69c3e2-7095-4372-87a0-0182be0a766f"), 150L);
        BookDTO updatedBook = new BookDTO(1L, "TitleUpdated", UUID.fromString("f3cf8fe6-648b-4a63-8f00-3397e7936622"), 150L);

        Mockito.when(bookService.update(eq(1L), any(BookDTO.class))).thenReturn(updatedBook);

        String jsonContent = "{\"id\":null,\"bookTitle\":\"TitleUpdated\",\"utilisateurId\":\"eb69c3e2-7095-4372-87a0-0182be0a766f\",\"shelfId\":150}";

        mockMvc.perform(put("/api/book/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookTitle").value("TitleUpdated"));
    }

    @Test
    @WithMockUser
    public void testDeleteBook() throws Exception {
        Mockito.doNothing().when(bookService).delete(1L);

        mockMvc.perform(delete("/api/book/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
