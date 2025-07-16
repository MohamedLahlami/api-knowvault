package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.ChapterDTO;

import java.util.List;

public interface ChapterService {
    ChapterDTO create(ChapterDTO dto);
    List<ChapterDTO> findAll();
    ChapterDTO findById(Long id);
    ChapterDTO update(Long id, ChapterDTO dto);
    void delete(Long id);
}
