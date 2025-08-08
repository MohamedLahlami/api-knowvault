package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.model.Tag;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.service.ShelfService;
import com.norsys.knowvault.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository shelfRepository;
    private final TagService tagService; // Injection du service Tag

    @Override
    public ShelfDTO create(ShelfDTO dto) {
        Shelf shelf = Shelf.builder()
                .label(dto.getLabel())
                .description(dto.getDescription())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        if (dto.getTag() != null && dto.getTag().getId() != null) {
            TagDTO tagDTO = tagService.findById(dto.getTag().getId());
            Tag tag = TagDTO.toEntity(tagDTO); // ✅ conversion
            shelf.setTag(tag);
        }

        Shelf savedShelf = shelfRepository.save(shelf);
        return ShelfDTO.toDto(savedShelf);
    }

    @Override
    public List<ShelfDTO> findAll() {
        List<Shelf> shelves = shelfRepository.findAll();
        return shelves.stream()
                .map(ShelfDTO::toDto)
                .toList();
    }

    @Override
    public ShelfDTO findById(Long id) {
        Shelf shelf = shelfRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Etagère introuvable"));
        return ShelfDTO.toDto(shelf);
    }

    @Override
    public ShelfDTO update(Long id, ShelfDTO dto) {
        Shelf shelf = shelfRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Etagère introuvable"));

        if (dto.getLabel() != null) shelf.setLabel(dto.getLabel());
        if (dto.getDescription() != null) shelf.setDescription(dto.getDescription());

        // Mise à jour du tag si présent dans le DTO
        if (dto.getTag() != null && dto.getTag().getId() != null) {
            TagDTO tagDTO = tagService.findById(dto.getTag().getId());
            Tag tag = TagDTO.toEntity(tagDTO); // ✅ conversion
            shelf.setTag(tag);
        }

        shelf.setUpdatedAt(OffsetDateTime.now());

        Shelf updatedShelf = shelfRepository.save(shelf);
        return ShelfDTO.toDto(updatedShelf);
    }

    @Override
    public Page<ShelfDTO> findAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shelf> shelfPage = shelfRepository.findAll(pageable);
        return shelfPage.map(ShelfDTO::toDto);
    }

    @Override
    public void delete(Long id) {
        if (!shelfRepository.existsById(id)) {
            throw new EntityNotFoundException("Etagère introuvable");
        }
        shelfRepository.deleteById(id);
    }
}
