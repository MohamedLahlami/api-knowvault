package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.TagDTO;

import java.util.List;
public interface TagService {
    TagDTO create(TagDTO tagDTO);
    TagDTO update(Long id, TagDTO tagDTO);
    void delete(Long id);
    List<TagDTO> findAll();
    TagDTO findById(Long id);
    List<TagDTO> findByTypeBook();
    List<TagDTO> findByTypeShelf();
}
