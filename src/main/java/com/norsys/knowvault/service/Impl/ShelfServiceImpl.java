package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.service.ShelfService;
import com.norsys.knowvault.service.TagService;
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
    private final TagService tagService;

    @Override
    public ShelfDTO create(ShelfDTO dto) {
        Shelf shelf = Shelf.builder()
                .label(dto.getLabel())
                .description(dto.getDescription())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        Shelf savedShelf = shelfRepository.save(shelf);

        if (dto.getTags() != null) {
            for (TagDTO tagDTO : dto.getTags()) {
                tagDTO.setResourceId(savedShelf.getId());
                tagDTO.setType(TagType.SHELF);
                tagService.createTag(tagDTO);
            }
        }

        List<TagDTO> tags = tagService.getTagsByShelfId(savedShelf.getId());
        return ShelfDTO.toDto(savedShelf, tags);
    }


    @Override
    public List<ShelfDTO> findAll() {
        List<Shelf> shelves = shelfRepository.findAll();
        return shelves.stream()
                .map(shelf -> {
                    List<TagDTO> tags = tagService.getTagsByShelfId(shelf.getId());
                    return ShelfDTO.toDto(shelf, tags);
                })
                .toList();
    }

    @Override
    public ShelfDTO findById(Long id) {
        return ShelfDTO.toDto(shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etagère introuvable")));
    }

    @Override
    public ShelfDTO findByIdWithTags(Long id) {
        Shelf shelf = shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etagère introuvable"));

        ShelfDTO dto = ShelfDTO.toDto(shelf);
        dto.setTags(tagService.getTagsByShelfId(id));
        return dto;
    }

    @Override
    public ShelfDTO update(Long id, ShelfDTO dto) {
        Shelf shelf = shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etagère introuvable"));

        if (dto.getLabel() != null) shelf.setLabel(dto.getLabel());
        if (dto.getDescription() != null) shelf.setDescription(dto.getDescription());
        shelf.setUpdatedAt(OffsetDateTime.now());


        if (dto.getTags() != null) {
            //Delete the old tags
            List<TagDTO> oldTags = tagService.getTagsByShelfId(id);
            for (TagDTO oldTag : oldTags) {
                tagService.deleteTag(oldTag.getId());
            }

            // create the new tags
            for (TagDTO newTag : dto.getTags()) {
                newTag.setResourceId(id);
                newTag.setType(com.norsys.knowvault.enumerator.TagType.SHELF);
                tagService.createTag(newTag);
            }
        }

        return ShelfDTO.toDto(shelfRepository.save(shelf));
    }

    @Override
    public Page<ShelfDTO> findAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shelf> shelfPage = shelfRepository.findAll(pageable);
        return shelfPage.map(shelf -> {
            List<TagDTO> tags = tagService.getTagsByShelfId(shelf.getId());
            return ShelfDTO.toDto(shelf, tags);
        });
    }

    @Override
    public void delete(Long id) {
        if (!shelfRepository.existsById(id)) {
            throw new RuntimeException("Etagère introuvable");
        }


        List<TagDTO> tags = tagService.getTagsByShelfId(id);
        for (TagDTO tag : tags) {
            tagService.deleteTag(tag.getId());
        }

        shelfRepository.deleteById(id);
    }
}
