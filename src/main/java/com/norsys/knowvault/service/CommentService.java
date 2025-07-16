package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO create(CommentDTO dto);
    List<CommentDTO> findAll();
    CommentDTO findById(Long id);
    CommentDTO update(Long id, CommentDTO dto);
    void delete(Long id);
}
