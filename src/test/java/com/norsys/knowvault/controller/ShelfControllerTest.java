package com.norsys.knowvault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.norsys.knowvault.config.TestSecurityConfig;
import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.service.Impl.FileStorageService;
import com.norsys.knowvault.service.ShelfService;
import com.norsys.knowvault.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShelfController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class ShelfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShelfService shelfService;

    @MockBean
    private TagService tagService;

    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    private ShelfDTO testShelfDTO;
    private TagDTO testTagDTO;

    @BeforeEach
    void setUp() {
        testTagDTO = new TagDTO();
        testTagDTO.setId(1L);
        testTagDTO.setLabel("Test Tag");

        testShelfDTO = new ShelfDTO();
        testShelfDTO.setId(1L);
        testShelfDTO.setLabel("Test Shelf");
        testShelfDTO.setDescription("Test shelf description");
        testShelfDTO.setTag(testTagDTO); // Use setTag instead of setTagId
        testShelfDTO.setImageName("test.png");
    }

    @Test
    @WithMockUser
    void testCreateShelf() throws Exception {
        // Given
        when(tagService.findById(1L)).thenReturn(testTagDTO);
        when(fileStorageService.saveFile(any())).thenReturn("/test/image.png");
        when(shelfService.create(any(ShelfDTO.class))).thenReturn(testShelfDTO);

        MockMultipartFile imageFile = new MockMultipartFile(
                "image", "test.png", "image/png", "test image content".getBytes());

        // When & Then
        mockMvc.perform(multipart("/api/shelf")
                .file(imageFile)
                .with(csrf())
                .param("label", "Test Shelf")
                .param("description", "Test shelf description")
                .param("tagId", "1"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.label").value("Test Shelf"));
    }

    @Test
    void testGetAllShelves() throws Exception {
        // Given - Test the paginated endpoint instead of findAll
        // Since the controller has pagination, we should test the status code only

        // When & Then
        mockMvc.perform(get("/api/shelf/paginated")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetShelfById() throws Exception {
        // Given
        when(shelfService.findById(1L)).thenReturn(testShelfDTO);

        // When & Then
        mockMvc.perform(get("/api/shelf/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.label").value("Test Shelf"));
    }

    @Test
    @WithMockUser
    void testUpdateShelf() throws Exception {
        // Given
        testShelfDTO.setLabel("Updated Shelf Label");
        when(shelfService.update(eq(1L), any(ShelfDTO.class))).thenReturn(testShelfDTO);

        // When & Then
        mockMvc.perform(put("/api/shelf/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testShelfDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.label").value("Updated Shelf Label"));
    }

    @Test
    @WithMockUser
    void testDeleteShelf() throws Exception {
        // Given - mock service to not throw any exception

        // When & Then
        mockMvc.perform(delete("/api/shelf/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
