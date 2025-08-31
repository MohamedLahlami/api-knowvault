package com.norsys.knowvault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.norsys.knowvault.config.RegisterUserFilter;
import com.norsys.knowvault.dto.ChapterDTO;
import com.norsys.knowvault.service.ChapterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChapterController.class,
           excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                                                 classes = RegisterUserFilter.class))
@ActiveProfiles("test")
class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChapterService chapterService;

    @Autowired
    private ObjectMapper objectMapper;

    private ChapterDTO testChapterDTO;

    @BeforeEach
    void setUp() {
        testChapterDTO = new ChapterDTO();
        testChapterDTO.setId(1L);
        testChapterDTO.setChapterTitle("Test Chapter");
        testChapterDTO.setBookId(1L);
    }

    @Test
    @WithMockUser
    void testCreateChapter() throws Exception {
        // Given
        when(chapterService.create(any(ChapterDTO.class))).thenReturn(testChapterDTO);

        // When & Then
        mockMvc.perform(post("/api/chapter")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testChapterDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.chapterTitle").value("Test Chapter"));
    }

    @Test
    @WithMockUser
    void testFindAllChapters() throws Exception {
        // Given
        List<ChapterDTO> chapters = Arrays.asList(testChapterDTO);
        when(chapterService.findAll()).thenReturn(chapters);

        // When & Then
        mockMvc.perform(get("/api/chapter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].chapterTitle").value("Test Chapter"));
    }

    @Test
    @WithMockUser
    void testFindChapterById() throws Exception {
        // Given
        when(chapterService.findById(1L)).thenReturn(testChapterDTO);

        // When & Then
        mockMvc.perform(get("/api/chapter/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.chapterTitle").value("Test Chapter"));
    }

    @Test
    @WithMockUser
    void testUpdateChapter() throws Exception {
        // Given
        testChapterDTO.setChapterTitle("Updated Chapter Title");
        when(chapterService.update(eq(1L), any(ChapterDTO.class))).thenReturn(testChapterDTO);

        // When & Then
        mockMvc.perform(put("/api/chapter/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testChapterDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.chapterTitle").value("Updated Chapter Title"));
    }

    @Test
    @WithMockUser
    void testDeleteChapter() throws Exception {
        // Given - mock service to not throw any exception

        // When & Then
        mockMvc.perform(delete("/api/chapter/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
