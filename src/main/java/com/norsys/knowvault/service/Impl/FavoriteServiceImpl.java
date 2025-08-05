package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.FavoriteDTO;
import com.norsys.knowvault.model.Favorite;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.model.Utilisateur;
import com.norsys.knowvault.repository.FavoriteRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PageRepository pageRepository;
    @Override
    public FavoriteDTO create(FavoriteDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Page page = pageRepository.findById(dto.getPageId())
                .orElseThrow(() -> new RuntimeException("Page introuvable"));

        Favorite favorite = Favorite.builder()
                .user(utilisateur)
                .page(page)
                .build();

        return FavoriteDTO.toDto(favoriteRepository.save(favorite));
    }

    @Override
    public List<FavoriteDTO> findAll() {
        return FavoriteDTO.toDtoList(favoriteRepository.findAll());
    }

    @Override
    public FavoriteDTO findById(Long id) {
        return FavoriteDTO.toDto(favoriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Favoris introuvable")));
    }

    @Override
    public FavoriteDTO update(Long id, FavoriteDTO dto) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Favoris introuvable"));

        if (dto.getUserId() != null) {
            Utilisateur u = utilisateurRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
            favorite.setUser(u);
        }

        if (dto.getPageId() != null) {
            Page p = pageRepository.findById(dto.getPageId())
                    .orElseThrow(() -> new RuntimeException("Page introuvable"));
            favorite.setPage(p);
        }

        return FavoriteDTO.toDto(favoriteRepository.save(favorite));
    }

    @Override
    public void delete(Long id) {
        if (!favoriteRepository.existsById(id)) {
            throw new RuntimeException("Favoris introuvable");
        }
        favoriteRepository.deleteById(id);
    }

@Override
    public List<FavoriteDTO> findByUserId(UUID userId) {
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(FavoriteDTO::toDto)
                .toList();
    }


}
