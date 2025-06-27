package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.UtilisateurDTO;

import java.util.List;

public interface UtilisateurService {
    List<UtilisateurDTO> getAll();
    UtilisateurDTO getUtilisateurByLogin(String login);

}
