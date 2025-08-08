package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagDTO> create(@RequestBody TagDTO dto) {
        TagDTO created = tagService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> findAll() {
        List<TagDTO> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> findById(@PathVariable Long id) {
        TagDTO tag = tagService.findById(id);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("/books")
    public ResponseEntity<List<TagDTO>> findBookTags() {
        List<TagDTO> tags = tagService.findByTypeBook();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/shelves")
    public ResponseEntity<List<TagDTO>> findShelfTags() {
        List<TagDTO> tags = tagService.findByTypeShelf();
        return ResponseEntity.ok(tags);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> update(@PathVariable Long id, @RequestBody TagDTO dto) {
        TagDTO updated = tagService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
