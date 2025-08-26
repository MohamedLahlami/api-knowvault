package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface CommentService {
    CommentDTO create(CommentDTO dto, Authentication authentication);
    Page<CommentDTO> findAll(Pageable pageable);
    Page<CommentDTO> searchByContent(String query, Pageable pageable);
    CommentDTO findById(Long id);
    CommentDTO update(Long id, CommentDTO dto);
    void delete(Long id);
}
