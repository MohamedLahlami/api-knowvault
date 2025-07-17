package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.HistoryDTO;
import com.norsys.knowvault.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping
    public ResponseEntity<HistoryDTO> create(@RequestBody HistoryDTO dto) {
        HistoryDTO created = historyService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HistoryDTO>> findAll() {
        List<HistoryDTO> historiques = historyService.findAll();
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoryDTO> findById(@PathVariable Long id) {
        HistoryDTO historique = historyService.findById(id);
        return ResponseEntity.ok(historique);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoryDTO> update(@PathVariable Long id, @RequestBody HistoryDTO dto) {
        HistoryDTO updated = historyService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
