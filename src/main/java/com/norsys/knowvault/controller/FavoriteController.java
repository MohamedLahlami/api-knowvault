package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.FavoriteDTO;
import com.norsys.knowvault.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorie")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteDTO> create(@RequestBody FavoriteDTO dto) {
        FavoriteDTO created = favoriteService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> findAll() {
        List<FavoriteDTO> favoris = favoriteService.findAll();
        return ResponseEntity.ok(favoris);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteDTO> findById(@PathVariable Long id) {
        FavoriteDTO favoris = favoriteService.findById(id);
        return ResponseEntity.ok(favoris);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FavoriteDTO> update(@PathVariable Long id, @RequestBody FavoriteDTO dto) {
        FavoriteDTO updated = favoriteService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        favoriteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
