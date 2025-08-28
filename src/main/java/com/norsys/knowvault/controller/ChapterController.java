package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.ChapterDTO;
import com.norsys.knowvault.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/chapter")
@RequiredArgsConstructor
@Slf4j
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping
    public ResponseEntity<ChapterDTO> create(@RequestBody ChapterDTO dto) {
        log.info("Creating new chapter '{}' for book ID: {}", dto.getChapterTitle(), dto.getBookId());
        try {
            ChapterDTO created = chapterService.create(dto);
            log.info("Successfully created chapter with ID: {} and title: '{}'", created.getId(), created.getChapterTitle());
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating chapter '{}': {}", dto.getChapterTitle(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<ChapterDTO>> findAll() {
        log.info("Fetching all chapters");
        try {
            List<ChapterDTO> chapitres = chapterService.findAll();
            log.info("Found {} chapters", chapitres.size());
            return ResponseEntity.ok(chapitres);
        } catch (Exception e) {
            log.error("Error fetching all chapters: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDTO> findById(@PathVariable Long id) {
        log.info("Fetching chapter with ID: {}", id);
        try {
            ChapterDTO chapitre = chapterService.findById(id);
            log.info("Successfully found chapter: '{}' (ID: {})", chapitre.getChapterTitle(), id);
            return ResponseEntity.ok(chapitre);
        } catch (Exception e) {
            log.error("Error fetching chapter with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChapterDTO> update(@PathVariable Long id, @RequestBody ChapterDTO dto) {
        log.info("Updating chapter with ID: {}, new title: '{}'", id, dto.getChapterTitle());
        try {
            ChapterDTO updated = chapterService.update(id, dto);
            log.info("Successfully updated chapter with ID: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating chapter with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting chapter with ID: {}", id);
        try {
            chapterService.delete(id);
            log.info("Successfully deleted chapter with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting chapter with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/book/{bookId}/chapters")
    public List<ChapterDTO> getChaptersByBook(@PathVariable Long bookId) {
        log.info("Fetching chapters for book ID: {}", bookId);
        try {
            List<ChapterDTO> chapters = chapterService.findByBookId(bookId);
            log.info("Found {} chapters for book ID: {}", chapters.size(), bookId);
            return chapters;
        } catch (Exception e) {
            log.error("Error fetching chapters for book ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }
}
