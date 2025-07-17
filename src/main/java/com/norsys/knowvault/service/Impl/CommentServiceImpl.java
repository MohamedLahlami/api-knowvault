package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.CommentDTO;
import com.norsys.knowvault.model.Comment;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.model.Utilisateur;
import com.norsys.knowvault.repository.CommentRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PageRepository pageRepository;

    @Override
    public CommentDTO create(CommentDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        Page page = pageRepository.findById(dto.getPageId())
                .orElseThrow(() -> new RuntimeException("Page introuvable"));

        Comment comment = Comment.builder()
                .text(dto.getText())
                .user(utilisateur)
                .page(page)
                .build();

        return CommentDTO.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDTO> findAll() {
        return CommentDTO.toDtoList(commentRepository.findAll());
    }

    @Override
    public CommentDTO findById(Long id) {
        return CommentDTO.toDto(commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire introuvable")));
    }

    @Override
    public CommentDTO update(Long id, CommentDTO dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire introuvable"));

        if (dto.getText() != null) comment.setText(dto.getText());

        if (dto.getUserId() != null) {
            Utilisateur u = utilisateurRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
            comment.setUser(u);
        }

        if (dto.getPageId() != null) {
            Page p = pageRepository.findById(dto.getPageId())
                    .orElseThrow(() -> new RuntimeException("Page introuvable"));
            comment.setPage(p);
        }

        return CommentDTO.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Commentaire introuvable");
        }
        commentRepository.deleteById(id);
    }

}
