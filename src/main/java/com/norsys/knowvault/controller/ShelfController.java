package com.norsys.knowvault.controller;


import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.service.Impl.FileStorageService;
import com.norsys.knowvault.service.ShelfService;
import com.norsys.knowvault.service.TagService;
import lombok.RequiredArgsConstructor;
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

        ShelfDTO dto = new ShelfDTO();
        dto.setLabel(label);
        dto.setDescription(description);

        if (tagId != null) {
            dto.setTag(tagService.findById(tagId));
        }

        if (image != null && !image.isEmpty()) {
            String fileName = fileStorageService.saveFile(image);
            dto.setImageName(fileName);
        }

        ShelfDTO created = shelfService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @GetMapping("/paginated")
    public ResponseEntity<Page<ShelfDTO>> getPaginatedShelves(
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<ShelfDTO> shelfPage = shelfService.findAllPaginated(page, 3);
        return ResponseEntity.ok(shelfPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfDTO> findById(@PathVariable Long id) {
        ShelfDTO etagere = shelfService.findById(id);
        return ResponseEntity.ok(etagere);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO>> getBooksByShelf(@PathVariable Long id) {
        List<BookDTO> books = shelfService.getBooksByShelfId(id);
        return ResponseEntity.ok(books);
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
