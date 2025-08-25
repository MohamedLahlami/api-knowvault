package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.CommentDTO;
import com.norsys.knowvault.model.Comment;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.repository.CommentRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PageRepository pageRepository;

    @Override
    public CommentDTO create(CommentDTO dto, Authentication authentication) {
        Page page = pageRepository.findById(dto.getPageId())
                .orElseThrow(() -> new RuntimeException("Page introuvable"));

        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = (Jwt) jwtAuth.getToken();
        String username = jwt.getClaim("preferred_username");

        Comment comment = Comment.builder()
                .text(dto.getText())
                .utilisateurLogin(username)
                .createdAt(LocalDateTime.now())
                .page(page)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentDTO.toDto(savedComment);
    }

    @Override
    public org.springframework.data.domain.Page<CommentDTO> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(CommentDTO::toDto);
    }

    @Override
    public org.springframework.data.domain.Page<CommentDTO> searchByContent(String query, Pageable pageable) {
        return commentRepository.findByTextContainingIgnoreCase(query, pageable)
                .map(CommentDTO::toDto);
    }

    @Override
    public CommentDTO findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire introuvable"));
        return CommentDTO.toDto(comment);
    }

    @Override
    public CommentDTO update(Long id, CommentDTO dto) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire introuvable"));

        existingComment.setText(dto.getText());

        if (dto.getPageId() != null) {
            Page page = pageRepository.findById(dto.getPageId())
                    .orElseThrow(() -> new RuntimeException("Page introuvable"));
            existingComment.setPage(page);
        }

        Comment updatedComment = commentRepository.save(existingComment);
        return CommentDTO.toDto(updatedComment);
    }

    @Override
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Commentaire introuvable");
        }
        commentRepository.deleteById(id);
    }
}
