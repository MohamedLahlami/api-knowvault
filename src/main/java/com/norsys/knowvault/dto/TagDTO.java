package com.norsys.knowvault.dto;

import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class TagDTO {

    private Long id;
    private String label;
    private TagType type;
    private Long resourceId;


    public TagDTO() {
    }

    public TagDTO(Long id, String label, TagType type, Long resourceId) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.resourceId = resourceId;
    }

    public static List<TagDTO> fromEntityList(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> new TagDTO(
                        tag.getId(),
                        tag.getLabel(),
                        tag.getType(),
                        tag.getResourceId()))
                .collect(Collectors.toList());
    }
}
