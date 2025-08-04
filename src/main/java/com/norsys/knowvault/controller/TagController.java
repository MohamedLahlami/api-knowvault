package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // GET Tags by Book ID
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<TagDTO>> getTagsByBook(@PathVariable Long bookId) {
        List<TagDTO> tags = tagService.getTagsByBookId(bookId);
        return ResponseEntity.ok(tags);
    }

    // GET Tags by Shelf ID
    @GetMapping("/shelf/{shelfId}")
    public ResponseEntity<List<TagDTO>> getTagsByShelf(@PathVariable Long shelfId) {
        List<TagDTO> tags = tagService.getTagsByShelfId(shelfId);
        return ResponseEntity.ok(tags);
    }

    // CREATE a new Tag
    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        TagDTO createdTag = tagService.createTag(tagDTO);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    // UPDATE an existing Tag
    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        TagDTO updatedTag = tagService.updateTag(id, tagDTO);
        return ResponseEntity.ok(updatedTag);
    }

    // DELETE a Tag by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
