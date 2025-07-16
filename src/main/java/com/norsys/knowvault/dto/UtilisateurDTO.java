package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Utilisateur;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class UtilisateurDTO {
    private UUID id;
    private String login;
    private String lastName;
    private String firstName;
    private String nomComplet;

    public static UtilisateurDTO toDto(Utilisateur utilisateur) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(utilisateur.getId());
        dto.setLogin(utilisateur.getLogin());
        dto.setLastName(utilisateur.getLastName());
        dto.setFirstName(utilisateur.getFirstName());
        dto.setNomComplet(utilisateur.getLastName() + " " + utilisateur.getFirstName());

        return dto;
    }

    public static List<UtilisateurDTO> toDtoList(List<Utilisateur> utilisateurs) {
        return utilisateurs.stream()
                .map(UtilisateurDTO::toDto)
                .collect(Collectors.toList());
    }
}
