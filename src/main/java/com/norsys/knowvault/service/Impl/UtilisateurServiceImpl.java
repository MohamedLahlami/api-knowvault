package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.UtilisateurDTO;
import com.norsys.knowvault.model.Utilisateur;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public List<UtilisateurDTO> getAll() {
        return UtilisateurDTO.toDtoList(utilisateurRepository.findAll());
    }

    @Override
    public UtilisateurDTO getUtilisateurByLogin(final String login) {
        return UtilisateurDTO.toDto(utilisateurRepository.getUtilisateurByLogin(login));
    }
    @Override
    public Utilisateur ensureUserExistsFromJwt(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return utilisateurRepository.findById(userId).orElseGet(() -> {
            Utilisateur user = new Utilisateur();
            user.setId(userId);
            user.setLogin(jwt.getClaimAsString("preferred_username"));
            user.setFirstName(jwt.getClaimAsString("given_name"));
            user.setLastName(jwt.getClaimAsString("family_name"));
            user.setCreatedAt(OffsetDateTime.now());
            user.setUpdatedAt(OffsetDateTime.now());

            return utilisateurRepository.save(user);
        });
    }


}
