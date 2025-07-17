package com.norsys.knowvault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.norsys.knowvault.dto.UtilisateurDTO;
import com.norsys.knowvault.service.UtilisateurService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UtilisateurController.class)
public class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilisateurService;

    private final UtilisateurDTO utilisateur1 = new UtilisateurDTO(
            UUID.randomUUID(),
            "login1",
            "nom1",
            "prenom1",
            "email1@example.com"
    );

    private final UtilisateurDTO utilisateur2 = new UtilisateurDTO(
            UUID.randomUUID(),
            "login2",
            "nom2",
            "prenom2",
            "email2@example.com"
    );

    @Test
    @DisplayName("GET /utilisateurs should return all users")
    @WithMockUser
    public void testGetAllUtilisateurs() throws Exception {
        Mockito.when(utilisateurService.getAll()).thenReturn(List.of(utilisateur1, utilisateur2));

        mockMvc.perform(get("/utilisateurs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].login").value("login1"))
                .andExpect(jsonPath("$[0].lastName").value("nom1"))
                .andExpect(jsonPath("$[0].firstName").value("prenom1"))
                .andExpect(jsonPath("$[1].login").value("login2"))
                .andExpect(jsonPath("$[1].lastName").value("nom2"))
                .andExpect(jsonPath("$[1].firstName").value("prenom2"));
    }

    @Test
    @DisplayName("GET /utilisateurs/{login} should return user by login")
    @WithMockUser
    public void testGetUtilisateurByLogin() throws Exception {
        Mockito.when(utilisateurService.getUtilisateurByLogin("login1")).thenReturn(utilisateur1);

        mockMvc.perform(get("/utilisateurs/login1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("login1"))
                .andExpect(jsonPath("$.lastName").value("nom1"))
                .andExpect(jsonPath("$.firstName").value("prenom1"))
                .andExpect(jsonPath("$.nomComplet").value("email1@example.com"));
    }
}
