package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.TagDTO;

import java.util.List;
public interface TagService {
    List<TagDTO> getTagsByBookId(Long bookId);
    List<TagDTO> getTagsByShelfId(Long shelfId);
    TagDTO createTag(TagDTO tagDTO);
    TagDTO updateTag(Long id, TagDTO tagDTO);
    void deleteTag(Long id);
}
