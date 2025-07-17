package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.PageDTO;

import java.util.List;

public interface PageService {
    PageDTO create(PageDTO dto);
    List<PageDTO> findAll();
    PageDTO findById(Long id);
    PageDTO update(Long id, PageDTO dto);
    void delete(Long id);
}

