package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.FavoriteDTO;

import java.util.List;

public interface FavoriteService {
    FavoriteDTO create(FavoriteDTO dto);
    List<FavoriteDTO> findAll();
    FavoriteDTO findById(Long id);
    FavoriteDTO update(Long id, FavoriteDTO dto);
    void delete(Long id);
}
