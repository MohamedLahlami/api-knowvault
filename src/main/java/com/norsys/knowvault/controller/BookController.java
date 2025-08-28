package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.service.BookService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> create(@RequestBody BookDTO dto, Authentication authentication) {
        log.info("Creating new book with title: '{}' by user: {}", dto.getTitle(), authentication.getName());
        try {
            BookDTO created = bookService.create(dto, authentication);
            log.info("Successfully created book with ID: {} and title: '{}'", created.getId(), created.getTitle());
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating book '{}': {}", dto.getTitle(), e.getMessage(), e);
            throw e;
        }
    }


    @GetMapping
    public ResponseEntity<Page<BookDTO>> findAll(Pageable pageable) {
        log.info("Fetching books with pagination - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<BookDTO> livres = bookService.findAll(pageable);
            log.info("Found {} books (page {} of {})", livres.getNumberOfElements(),
                    livres.getNumber() + 1, livres.getTotalPages());
            return ResponseEntity.ok(livres);
        } catch (Exception e) {
            log.error("Error fetching books with pagination: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/public")
    @PermitAll
    public List<BookDTO> getAllBooksPublic() {
        log.info("Fetching all public books");
        try {
            List<BookDTO> books = bookService.findAllBooks();
            log.info("Found {} public books", books.size());
            return books;
        } catch (Exception e) {
            log.error("Error fetching public books: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookDTO>> searchBooks(
            @RequestParam("q") String query,
            Pageable pageable) {
        log.info("Searching books with query: '{}', page: {}, size: {}", query, pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<BookDTO> result = bookService.searchByTitle(query, pageable);
            log.info("Search for '{}' returned {} results", query, result.getNumberOfElements());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error searching books with query '{}': {}", query, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
        log.info("Fetching book with ID: {}", id);
        try {
            BookDTO livre = bookService.findById(id);
            log.info("Successfully found book: '{}' (ID: {})", livre.getTitle(), id);
            return ResponseEntity.ok(livre);
        } catch (Exception e) {
            log.error("Error fetching book with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO dto) {
        log.info("Updating book with ID: {}, new title: '{}'", id, dto.getTitle());
        try {
            BookDTO updated = bookService.update(id, dto);
            log.info("Successfully updated book with ID: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating book with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting book with ID: {}", id);
        try {
            bookService.delete(id);
            log.info("Successfully deleted book with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting book with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

}
