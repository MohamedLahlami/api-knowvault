package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Tag;
import com.norsys.knowvault.repository.TagRepository;
import com.norsys.knowvault.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagDTO create(TagDTO tagDTO) {
        Tag tag = Tag.builder()
                .label(tagDTO.getLabel())
                .type(tagDTO.getType())
                .build();

        Tag saved = tagRepository.save(tag);
        return TagDTO.toDto(saved);
    }

    @Override
    public List<TagDTO> findAll() {
        return TagDTO.toDtoList(tagRepository.findAll());
    }

    @Override
    public TagDTO findById(Long id) {
        return tagRepository.findById(id)
                .map(TagDTO::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));
    }

    @Override
    public List<TagDTO> findByTypeBook() {
        return TagDTO.toDtoList(tagRepository.findByType(TagType.BOOK));
    }

    @Override
    public List<TagDTO> findByTypeShelf() {
        return TagDTO.toDtoList(tagRepository.findByType(TagType.SHELF));
    }

    @Override
    public TagDTO update(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));

        tag.setLabel(tagDTO.getLabel());
        tag.setType(tagDTO.getType());

        Tag updated = tagRepository.save(tag);
        return TagDTO.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }

}
