package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.PageDTO;
import com.norsys.knowvault.service.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/page")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Slf4j
public class PageController {

    private final PageService pageService;

    @PostMapping
    public ResponseEntity<PageDTO> create(@RequestBody PageDTO dto) {
        log.info("Creating new page with pageNumber: {}", dto.getPageNumber());
        try {
            PageDTO created = pageService.create(dto);
            log.info("Successfully created page with ID: {}", created.getId());
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating page: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<PageDTO>> findAll() {
        log.info("Fetching all pages");
        try {
            List<PageDTO> pages = pageService.findAll();
            log.info("Found {} pages", pages.size());
            return ResponseEntity.ok(pages);
        } catch (Exception e) {
            log.error("Error fetching all pages: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PageDTO> findById(@PathVariable Long id) {
        log.info("Fetching page with ID: {}", id);
        try {
            PageDTO page = pageService.findById(id);
            log.info("Successfully found page with ID: {}", id);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            log.error("Error fetching page with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PageDTO> update(@PathVariable Long id, @RequestBody PageDTO dto) {
        log.info("Updating page with ID: {}", id);
        try {
            PageDTO updated = pageService.update(id, dto);
            log.info("Successfully updated page with ID: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating page with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting page with ID: {}", id);
        try {
            pageService.delete(id);
            log.info("Successfully deleted page with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting page with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<PageDTO>> getPagesByChapter(@PathVariable Long chapterId) {
        log.info("Fetching pages for chapter ID: {}", chapterId);
        try {
            List<PageDTO> pages = pageService.findByChapterId(chapterId);
            log.info("Found {} pages for chapter ID: {}", pages.size(), chapterId);
            return ResponseEntity.ok(pages);
        } catch (Exception e) {
            log.error("Error fetching pages for chapter ID {}: {}", chapterId, e.getMessage(), e);
            throw e;
        }
    }
}
