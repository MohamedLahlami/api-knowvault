package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.CommentDTO;
import com.norsys.knowvault.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> create(@RequestBody CommentDTO dto) {
        CommentDTO created = commentService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> findAll() {
        List<CommentDTO> commentaires = commentService.findAll();
        return ResponseEntity.ok(commentaires);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> findById(@PathVariable Long id) {
        CommentDTO commentaire = commentService.findById(id);
        return ResponseEntity.ok(commentaire);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> update(@PathVariable Long id, @RequestBody CommentDTO dto) {
        CommentDTO updated = commentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
