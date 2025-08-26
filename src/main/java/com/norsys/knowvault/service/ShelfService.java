package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.dto.ShelfDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShelfService {
    ShelfDTO create(ShelfDTO dto);
    List<ShelfDTO> findAll();
    ShelfDTO findById(Long id);
    ShelfDTO update(Long id, ShelfDTO dto);
    ShelfDTO incrementViews(Long id);
    Page<ShelfDTO> findAllPaginated(int page, int size);
    List<BookDTO> getBooksByShelfId(Long shelfId);
    void delete(Long id);
}
