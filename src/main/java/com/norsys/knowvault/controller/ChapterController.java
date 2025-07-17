package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.ChapterDTO;
import com.norsys.knowvault.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapter")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping
    public ResponseEntity<ChapterDTO> create(@RequestBody ChapterDTO dto) {
        ChapterDTO created = chapterService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ChapterDTO>> findAll() {
        List<ChapterDTO> chapitres = chapterService.findAll();
        return ResponseEntity.ok(chapitres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDTO> findById(@PathVariable Long id) {
        ChapterDTO chapitre = chapterService.findById(id);
        return ResponseEntity.ok(chapitre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChapterDTO> update(@PathVariable Long id, @RequestBody ChapterDTO dto) {
        ChapterDTO updated = chapterService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chapterService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
