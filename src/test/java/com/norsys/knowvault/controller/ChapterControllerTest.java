package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.ChapterDTO;
import com.norsys.knowvault.service.ChapterService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChapterController.class)
public class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChapterService chapterService;

    @Test
    @WithMockUser
    public void testFindAllChapters() throws Exception {
        List<ChapterDTO> chapters = Arrays.asList(
                new ChapterDTO(1L, "Chapter 1", 1L),
                new ChapterDTO(2L, "Chapter 2", 2L)
        );

        Mockito.when(chapterService.findAll()).thenReturn(chapters);

        mockMvc.perform(get("/api/chapter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].chapterTitle").value("Chapter 1"))
                .andExpect(jsonPath("$[0].bookId").value(1))
                .andExpect(jsonPath("$[1].chapterTitle").value("Chapter 2"))
                .andExpect(jsonPath("$[1].bookId").value(2));
    }

    @Test
    @WithMockUser
    public void testFindById() throws Exception {
        ChapterDTO chapter = new ChapterDTO(1L, "Chapter 1", 1L);

        Mockito.when(chapterService.findById(1L)).thenReturn(chapter);

        mockMvc.perform(get("/api/chapter/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.chapterTitle").value("Chapter 1"))
                .andExpect(jsonPath("$.bookId").value(1));
    }

    @Test
    @WithMockUser
    public void testCreateChapter() throws Exception {
        ChapterDTO createdChapter = new ChapterDTO(1L, "New Chapter", 1L);

        Mockito.when(chapterService.create(any(ChapterDTO.class))).thenReturn(createdChapter);

        String jsonContent = "{\"id\":null,\"chapterTitle\":\"New Chapter\",\"bookId\":1}";

        mockMvc.perform(post("/api/chapter")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.chapterTitle").value("New Chapter"))
                .andExpect(jsonPath("$.bookId").value(1));
    }

    @Test
    @WithMockUser
    public void testUpdateChapter() throws Exception {
        ChapterDTO updatedChapter = new ChapterDTO(1L, "Updated Chapter", 1L);

        Mockito.when(chapterService.update(eq(1L), any(ChapterDTO.class))).thenReturn(updatedChapter);

        String jsonContent = "{\"id\":null,\"chapterTitle\":\"Updated Chapter\",\"bookId\":1}";

        mockMvc.perform(put("/api/chapter/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.chapterTitle").value("Updated Chapter"))
                .andExpect(jsonPath("$.bookId").value(1));
    }

    @Test
    @WithMockUser
    public void testDeleteChapter() throws Exception {
        Mockito.doNothing().when(chapterService).delete(1L);

        mockMvc.perform(delete("/api/chapter/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
