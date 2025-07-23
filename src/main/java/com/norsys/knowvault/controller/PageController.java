package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.PageDTO;
import com.norsys.knowvault.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/page")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @PostMapping
    public ResponseEntity<PageDTO> create(@RequestBody PageDTO dto) {
        PageDTO created = pageService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PageDTO>> findAll() {
        List<PageDTO> pages = pageService.findAll();
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PageDTO> findById(@PathVariable Long id) {
        PageDTO page = pageService.findById(id);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PageDTO> update(@PathVariable Long id, @RequestBody PageDTO dto) {
        PageDTO updated = pageService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
