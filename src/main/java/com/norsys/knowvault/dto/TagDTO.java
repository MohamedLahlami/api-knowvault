package com.norsys.knowvault.dto;

import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Tag;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {

    private Long id;
    private String label;
    private TagType type;
    private long value;

    public static TagDTO toDto(Tag tag) {
        if (tag == null) {
            return null;
        }

        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setLabel(tag.getLabel());
        dto.setType(tag.getType());
        dto.setValue(0);
        return dto;
    }

    public static List<TagDTO> toDtoList(List<Tag> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream()
                .map(TagDTO::toDto)
                .toList();
    }

    public static Tag toEntity(TagDTO dto) {
        if (dto == null) return null;
        return Tag.builder()
                .id(dto.getId())
                .label(dto.getLabel())
                .type(dto.getType())
                .build();
    }

}
