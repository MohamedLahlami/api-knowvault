package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.ShelfDTO;

import java.util.List;

public interface ShelfService {
    ShelfDTO create(ShelfDTO dto);
    List<ShelfDTO> findAll();
    ShelfDTO findById(Long id);
    ShelfDTO update(Long id, ShelfDTO dto);
    void delete(Long id);
}
