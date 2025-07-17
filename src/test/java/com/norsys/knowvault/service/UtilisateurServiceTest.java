package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.UtilisateurDTO;
import com.norsys.knowvault.model.Utilisateur;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.Impl.UtilisateurServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurServiceImplTest {

    private UtilisateurRepository utilisateurRepository;
    private UtilisateurServiceImpl utilisateurService;

    private final UUID fixedId = UUID.fromString("74aa141f-920c-4997-b41d-5aa5e8f72319");
    private OffsetDateTime now;

    @BeforeEach
    void setUp() {
        utilisateurRepository = mock(UtilisateurRepository.class);
        utilisateurService = new UtilisateurServiceImpl(utilisateurRepository);
        now = OffsetDateTime.now();  // On initialise ici pour avoir une date cohérente
    }

    @Test
    void testGetAll() {
        Utilisateur u1 = new Utilisateur(fixedId, "user1", "Doe", "John", "John Doe", now, now);
        Utilisateur u2 = new Utilisateur(UUID.randomUUID(), "user2", "Smith", "Jane", "Jane Smith", now, now);

        when(utilisateurRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<UtilisateurDTO> result = utilisateurService.getAll();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getLogin());
        assertEquals("user2", result.get(1).getLogin());
        assertEquals(fixedId, result.get(0).getId());
    }


}
