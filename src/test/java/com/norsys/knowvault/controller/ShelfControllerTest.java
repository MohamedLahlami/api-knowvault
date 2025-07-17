package com.norsys.knowvault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.service.ShelfService;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShelfController.class)
public class ShelfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShelfService shelfService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testCreateShelf() throws Exception {
        ShelfDTO dtoCreated = new ShelfDTO(1L, "LabelNew", "DescriptionNew", null);

        Mockito.when(shelfService.create(Mockito.any(ShelfDTO.class))).thenReturn(dtoCreated);

        ShelfDTO dtoRequest = new ShelfDTO();
        dtoRequest.setLabel("LabelNew");
        dtoRequest.setDescription("DescriptionNew");
        dtoRequest.setTag(null);

        String jsonRequest = objectMapper.writeValueAsString(dtoRequest);

        mockMvc.perform(post("/api/shelf")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label", is("LabelNew")))
                .andExpect(jsonPath("$.description", is("DescriptionNew")));
    }

    @Test
    @WithMockUser
    public void testFindAllShelves() throws Exception {
        List<ShelfDTO> shelves = Arrays.asList(
                new ShelfDTO(1L, "Label1", "Description1", null),
                new ShelfDTO(2L, "Label2", "Description2", null)
        );

        Mockito.when(shelfService.findAll()).thenReturn(shelves);

        mockMvc.perform(get("/api/shelf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].label", is("Label1")))
                .andExpect(jsonPath("$[1].label", is("Label2")));
    }

    @Test
    @WithMockUser
    public void testFindById() throws Exception {
        ShelfDTO dto = new ShelfDTO(1L, "Label1", "Description1", null);

        Mockito.when(shelfService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/shelf/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.label", is("Label1")))
                .andExpect(jsonPath("$.description", is("Description1")));
    }

    @Test
    @WithMockUser
    public void testUpdateShelf() throws Exception {
        ShelfDTO dtoUpdated = new ShelfDTO(1L, "LabelUpdated", "DescriptionUpdated", null);

        Mockito.when(shelfService.update(Mockito.eq(1L), Mockito.any(ShelfDTO.class))).thenReturn(dtoUpdated);

        ShelfDTO dtoRequest = new ShelfDTO();
        dtoRequest.setLabel("LabelUpdated");
        dtoRequest.setDescription("DescriptionUpdated");
        dtoRequest.setTag(null);

        String jsonRequest = objectMapper.writeValueAsString(dtoRequest);

        mockMvc.perform(put("/api/shelf/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label", is("LabelUpdated")))
                .andExpect(jsonPath("$.description", is("DescriptionUpdated")));
    }

    @Test
    @WithMockUser
    public void testDeleteShelf() throws Exception {
        Mockito.doNothing().when(shelfService).delete(1L);

        mockMvc.perform(delete("/api/shelf/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
