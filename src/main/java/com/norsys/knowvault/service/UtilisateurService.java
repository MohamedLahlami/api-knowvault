package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.UtilisateurDTO;
import com.norsys.knowvault.model.Utilisateur;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UtilisateurService {
    List<UtilisateurDTO> getAll();
    UtilisateurDTO getUtilisateurByLogin(String login);
    public Utilisateur ensureUserExistsFromJwt(Jwt jwt) ;


}
