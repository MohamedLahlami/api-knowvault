package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Tag;
import com.norsys.knowvault.repository.TagRepository;
import com.norsys.knowvault.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<TagDTO> getTagsByBookId(Long bookId) {
        List<Tag> tags = tagRepository.findByTypeAndResourceId(TagType.BOOK, bookId);
        return tags.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> getTagsByShelfId(Long shelfId) {
        List<Tag> tags = tagRepository.findByTypeAndResourceId(TagType.SHELF, shelfId);
        return tags.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setLabel(tagDTO.getLabel());
        tag.setType(tagDTO.getType());
        tag.setResourceId(tagDTO.getResourceId());

        tag = tagRepository.save(tag);

        return mapToDTO(tag);
    }

    @Override
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id " + id));

        existingTag.setLabel(tagDTO.getLabel());
        existingTag.setType(tagDTO.getType());
        existingTag.setResourceId(tagDTO.getResourceId());

        Tag updatedTag = tagRepository.save(existingTag);
        return mapToDTO(updatedTag);
    }

    @Override
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found with id " + id);
        }
        tagRepository.deleteById(id);
    }

    private TagDTO mapToDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setLabel(tag.getLabel());
        dto.setType(tag.getType());
        dto.setResourceId(tag.getResourceId());
        return dto;
    }
}
