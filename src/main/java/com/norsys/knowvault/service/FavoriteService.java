package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.FavoriteDTO;

import java.util.List;
import java.util.UUID;

public interface FavoriteService {

    List<FavoriteDTO> getFavoritesByUser();
   List<FavoriteDTO> OnlyFavoritesForUser();
    void deleteFavoriteById(Long id);
    FavoriteDTO toggleFavorite(Long pageId);
}
