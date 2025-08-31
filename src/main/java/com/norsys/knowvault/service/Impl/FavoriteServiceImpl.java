package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.FavoriteDTO;
import com.norsys.knowvault.exception.FavoriteNotFoundException;
import com.norsys.knowvault.model.*;
import com.norsys.knowvault.repository.FavoriteRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.FavoriteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PageRepository pageRepository;

    private UUID getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(jwt.getClaimAsString("sub"));
    }

    private Utilisateur getCurrentUser() {
        return utilisateurRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    @Override
    @Transactional
    public void deleteFavoriteById(Long id) {
        if (!favoriteRepository.existsById(id)) {
            throw new FavoriteNotFoundException("Favori non trouvé avec id: " + id);
        }
        favoriteRepository.deleteById(id);
    }

    @Override
    public List<FavoriteDTO> getFavoritesByUser() {
        Utilisateur user = getCurrentUser();
        return favoriteRepository.findByUser(user)
                .stream()
                .map(fav -> {
                    FavoriteDTO dto = new FavoriteDTO();
                    dto.setId(fav.getId());
                    dto.setUserId(user.getId());
                    dto.setPageId(fav.getPage().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public FavoriteDTO toggleFavorite(Long pageId) {
        Utilisateur user = getCurrentUser();
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page introuvable"));

        Optional<Favorite> existing = favoriteRepository.findByUserAndPage(user, page);
        if (existing.isPresent()) {
            favoriteRepository.delete(existing.get());
            return null;
        } else {
            Favorite fav = Favorite.builder()
                    .user(user)
                    .page(page)
                    .build();
            Favorite saved = favoriteRepository.save(fav);
            FavoriteDTO dto = new FavoriteDTO();
            dto.setId(saved.getId());
            dto.setUserId(user.getId());
            dto.setPageId(page.getId());
            dto.setPageNumber(page.getPageNumber());
            dto.setChapterTitle(page.getChapter().getChapterTitle());
            dto.setBookTitle(page.getChapter().getBook().getBookTitle());
            return dto;
        }
    }

    @Override
    public List<FavoriteDTO> onlyFavoritesForUser() {
        Utilisateur user = getCurrentUser();

        return favoriteRepository.findByUser(user)
                .stream()
                .map(fav -> {
                    Page page = fav.getPage();
                    Chapter chapter = page.getChapter();
                    Book book = chapter.getBook();

                    FavoriteDTO dto = new FavoriteDTO();
                    dto.setId(fav.getId());
                    dto.setUserId(user.getId());
                    dto.setPageId(page.getId());
                    dto.setPageNumber(page.getPageNumber());
                    dto.setChapterTitle(chapter.getChapterTitle());
                    dto.setBookTitle(book.getBookTitle());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
