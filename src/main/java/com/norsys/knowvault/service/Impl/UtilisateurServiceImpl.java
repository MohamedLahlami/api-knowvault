package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.UtilisateurDTO;
import com.norsys.knowvault.model.Utilisateur;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
