package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.FavoriteDTO;
import com.norsys.knowvault.exception.FavoriteNotFoundException;
import com.norsys.knowvault.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> getFavorites() {
        List<FavoriteDTO> list = favoriteService.getFavoritesByUser();
        return ResponseEntity.ok(list);
    }
    @PostMapping("/toggle/{pageId}")
    public ResponseEntity<FavoriteDTO> toggleFavorite(@PathVariable Long pageId) {
        FavoriteDTO dto = favoriteService.toggleFavorite(pageId);
        if (dto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/only")
    public ResponseEntity<List<FavoriteDTO>> getOnlyFavoritesForUser() {
        List<FavoriteDTO> favorites = favoriteService.onlyFavoritesForUser();
        return ResponseEntity.ok(favorites);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        try {
            favoriteService.deleteFavoriteById(id);
            return ResponseEntity.noContent().build();
        }
        catch (FavoriteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
