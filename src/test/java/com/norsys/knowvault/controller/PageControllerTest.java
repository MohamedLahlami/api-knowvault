package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.PageDTO;
import com.norsys.knowvault.enumerator.PageStatus;
import com.norsys.knowvault.service.PageService;
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

@WebMvcTest(PageController.class)
public class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageService pageService;

    @Test
    @WithMockUser
    public void testFindAllPages() throws Exception {
        List<PageDTO> pages = Arrays.asList(
                new PageDTO(1L, 1, "Title 1", "Content 1", PageStatus.Published, 1L),
                new PageDTO(2L, 2, "Title 2", "Content 2", PageStatus.Draft, 2L)
        );

        Mockito.when(pageService.findAll()).thenReturn(pages);

        mockMvc.perform(get("/api/page"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].content").value("Title 1"))
                .andExpect(jsonPath("$[0].markDownContent").value("Content 1"))
                .andExpect(jsonPath("$[0].status").value("Published"))
                .andExpect(jsonPath("$[0].chapterId").value(1))
                .andExpect(jsonPath("$[1].content").value("Title 2"))
                .andExpect(jsonPath("$[1].markDownContent").value("Content 2"))
                .andExpect(jsonPath("$[1].status").value("Draft"))
                .andExpect(jsonPath("$[1].chapterId").value(2));
    }

    @Test
    @WithMockUser
    public void testFindById() throws Exception {
        PageDTO page = new PageDTO(1L, 1, "Title 1", "Content 1", PageStatus.Published, 1L);

        Mockito.when(pageService.findById(1L)).thenReturn(page);

        mockMvc.perform(get("/api/page/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("Title 1"))
                .andExpect(jsonPath("$.markDownContent").value("Content 1"))
                .andExpect(jsonPath("$.status").value("Published"))
                .andExpect(jsonPath("$.chapterId").value(1));
    }

    @Test
    @WithMockUser
    public void testCreatePage() throws Exception {
        PageDTO createdPage = new PageDTO(1L, 1, "New Title", "New Content", PageStatus.Published, 1L);

        Mockito.when(pageService.create(any(PageDTO.class))).thenReturn(createdPage);

        String jsonContent = "{\"id\":null,\"pageNumber\":1,\"content\":\"New Title\",\"markDownContent\":\"New Content\",\"status\":\"Published\",\"chapterId\":1}";

        mockMvc.perform(post("/api/page")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("New Title"))
                .andExpect(jsonPath("$.markDownContent").value("New Content"))
                .andExpect(jsonPath("$.status").value("Published"))
                .andExpect(jsonPath("$.chapterId").value(1));
    }

    @Test
    @WithMockUser
    public void testUpdatePage() throws Exception {
        PageDTO updatedPage = new PageDTO(1L, 1, "Updated Title", "Updated Content", PageStatus.Draft, 1L);

        Mockito.when(pageService.update(eq(1L), any(PageDTO.class))).thenReturn(updatedPage);

        String jsonContent = "{\"id\":null,\"pageNumber\":1,\"content\":\"Updated Title\",\"markDownContent\":\"Updated Content\",\"status\":\"Draft\",\"chapterId\":1}";

        mockMvc.perform(put("/api/page/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("Updated Title"))
                .andExpect(jsonPath("$.markDownContent").value("Updated Content"))
                .andExpect(jsonPath("$.status").value("Draft"))
                .andExpect(jsonPath("$.chapterId").value(1));
    }

    @Test
    @WithMockUser
    public void testDeletePage() throws Exception {
        Mockito.doNothing().when(pageService).delete(1L);

        mockMvc.perform(delete("/api/page/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
