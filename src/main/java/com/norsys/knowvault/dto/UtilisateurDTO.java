package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Utilisateur;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UtilisateurDTO {
    private Long id;
    private String login;
    private String nom;
    private String prenom;
    private String nomComplet;

    public static UtilisateurDTO toDto(Utilisateur utilisateur) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(utilisateur.getId());
        dto.setLogin(utilisateur.getLogin());
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setNomComplet(utilisateur.getNom() + " " + utilisateur.getPrenom());

        return dto;
    }

    public static List<UtilisateurDTO> toDtoList(List<Utilisateur> utilisateurs) {
        return utilisateurs.stream()
                .map(UtilisateurDTO::toDto)
                .collect(Collectors.toList());
    }
}
