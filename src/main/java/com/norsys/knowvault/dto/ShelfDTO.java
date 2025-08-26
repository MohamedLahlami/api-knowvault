package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Shelf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShelfDTO {
    private Long id;
    private String label;
    private String description;
    private int bookCount;
    private String imageName;
    private String imageUrl;
    private long views;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private TagDTO tag;

    public static ShelfDTO toDto(Shelf e) {
        if (e == null) return null;

        ShelfDTO dto = new ShelfDTO();
        dto.setId(e.getId());
        dto.setLabel(e.getLabel());
        dto.setDescription(e.getDescription());
        dto.setImageName(e.getImageName());
        dto.setViews(e.getViews());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        dto.setBookCount(e.getBooks() != null ? e.getBooks().size() : 0);

        if (e.getTag() != null) {
            dto.setTag(TagDTO.toDto(e.getTag()));
        }

        return dto;
    }

    public static List<ShelfDTO> toDtoList(List<Shelf> list) {
        return list.stream().map(ShelfDTO::toDto).collect(Collectors.toList());
    }
}
