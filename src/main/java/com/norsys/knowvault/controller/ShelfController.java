package com.norsys.knowvault.controller;


import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelf")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ShelfController {
    private final ShelfService shelfService;

    @PostMapping
    public ResponseEntity<ShelfDTO> create(@RequestBody ShelfDTO dto) {
        ShelfDTO created = shelfService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ShelfDTO>> findAll() {
        List<ShelfDTO> etageres = shelfService.findAll();
        return ResponseEntity.ok(etageres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfDTO> findById(@PathVariable Long id) {
        ShelfDTO etagere = shelfService.findById(id);
        return ResponseEntity.ok(etagere);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelfDTO> update(@PathVariable Long id, @RequestBody ShelfDTO dto) {
        ShelfDTO updated = shelfService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shelfService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
