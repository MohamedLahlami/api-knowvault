package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.UtilisateurDTO;
import com.norsys.knowvault.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurService.getAll();
    }

    @GetMapping("{login}")
    public UtilisateurDTO getUtilisateurByLogin(@PathVariable final String login) {
        return utilisateurService.getUtilisateurByLogin(login);
    }

}
