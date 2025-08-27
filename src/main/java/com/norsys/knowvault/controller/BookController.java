package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.service.BookService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> create(@RequestBody BookDTO dto, Authentication authentication) {
        BookDTO created = bookService.create(dto, authentication);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<Page<BookDTO>> findAll(Pageable pageable) {
        Page<BookDTO> livres = bookService.findAll(pageable);
        return ResponseEntity.ok(livres);
    }

    @GetMapping("/public")
    @PermitAll
    public List<BookDTO> getAllBooksPublic() {
        List<BookDTO> books = bookService.findAllBooks();
        return books;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookDTO>> searchBooks(
            @RequestParam("q") String query,
            Pageable pageable) {
        Page<BookDTO> result = bookService.searchByTitle(query, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
        BookDTO livre = bookService.findById(id);
        return ResponseEntity.ok(livre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO dto) {
        BookDTO updated = bookService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
