package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.ShelfDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShelfService {
    ShelfDTO create(ShelfDTO dto);
    List<ShelfDTO> findAll();
    ShelfDTO findById(Long id);
    ShelfDTO update(Long id, ShelfDTO dto);
    Page<ShelfDTO> findAllPaginated(int page, int size);
    void delete(Long id);
}
