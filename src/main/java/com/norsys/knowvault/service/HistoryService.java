package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.HistoryDTO;

import java.util.List;

public interface HistoryService {
    HistoryDTO create(HistoryDTO dto);
    List<HistoryDTO> findAll();
    HistoryDTO findById(Long id);
    HistoryDTO update(Long id, HistoryDTO dto);
    void delete(Long id);
}
