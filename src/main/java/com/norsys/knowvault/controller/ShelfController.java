package com.norsys.knowvault.controller;


import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.service.Impl.FileStorageService;
import com.norsys.knowvault.service.ShelfService;
import com.norsys.knowvault.service.TagService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/shelf")
@RequiredArgsConstructor
@Slf4j
public class ShelfController {
    private final ShelfService shelfService;
    private final TagService tagService;
    private final FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShelfDTO> createShelf(
            @RequestParam("label") String label,
            @RequestParam("description") String description,
            @RequestParam("tagId") Long tagId,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        log.info("Creating new shelf with label: '{}', tagId: {}, hasImage: {}",
                label, tagId, image != null && !image.isEmpty());

        try {
            ShelfDTO dto = new ShelfDTO();
            dto.setLabel(label);
            dto.setDescription(description);

            if (tagId != null) {
                log.debug("Setting tag with ID: {} for shelf", tagId);
                dto.setTag(tagService.findById(tagId));
            }

            if (image != null && !image.isEmpty()) {
                log.debug("Processing image upload for shelf, file size: {} bytes", image.getSize());
                String fileName = fileStorageService.saveFile(image);
                dto.setImageName(fileName);
                log.debug("Saved image with filename: {}", fileName);
            }

            ShelfDTO created = shelfService.create(dto);
            log.info("Successfully created shelf with ID: {} and label: '{}'", created.getId(), created.getLabel());
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating shelf '{}': {}", label, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ShelfDTO>> getPaginatedShelves(
            @RequestParam(defaultValue = "0") int page) {
        log.info("Fetching paginated shelves - page: {}, size: 3", page);
        try {
            Page<ShelfDTO> shelfPage = shelfService.findAllPaginated(page, 3);
            log.info("Found {} shelves on page {} of {}",
                    shelfPage.getNumberOfElements(), shelfPage.getNumber() + 1, shelfPage.getTotalPages());
            return ResponseEntity.ok(shelfPage);
        } catch (Exception e) {
            log.error("Error fetching paginated shelves: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfDTO> findById(@PathVariable Long id) {
        log.info("Fetching shelf with ID: {}", id);
        try {
            ShelfDTO etagere = shelfService.findById(id);
            log.info("Successfully found shelf: '{}' (ID: {})", etagere.getLabel(), id);
            return ResponseEntity.ok(etagere);
        } catch (Exception e) {
            log.error("Error fetching shelf with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO>> getBooksByShelf(@PathVariable Long id) {
        log.info("Fetching books for shelf ID: {}", id);
        try {
            List<BookDTO> books = shelfService.getBooksByShelfId(id);
            log.info("Found {} books for shelf ID: {}", books.size(), id);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            log.error("Error fetching books for shelf ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/public")
    @PermitAll
    public List<ShelfDTO> getAllShelvesPublic() {
        log.info("Fetching all public shelves");
        try {
            List<ShelfDTO> etageres = shelfService.findAll();
            log.info("Found {} public shelves", etageres.size());
            return etageres;
        } catch (Exception e) {
            log.error("Error fetching public shelves: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelfDTO> update(@PathVariable Long id, @RequestBody ShelfDTO dto) {
        log.info("Updating shelf with ID: {}, new label: '{}'", id, dto.getLabel());
        try {
            ShelfDTO updated = shelfService.update(id, dto);
            log.info("Successfully updated shelf with ID: {}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating shelf with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting shelf with ID: {}", id);
        try {
            shelfService.delete(id);
            log.info("Successfully deleted shelf with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting shelf with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
