package com.norsys.knowvault.dto;

import com.norsys.knowvault.enumerator.Tag;
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
    private Tag tag;
    private int bookCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static ShelfDTO toDto(Shelf e) {
        if (e == null) return null;

        ShelfDTO dto = new ShelfDTO();
        dto.setId(e.getId());
        dto.setLabel(e.getLabel());
        dto.setDescription(e.getDescription());
        dto.setTag(e.getTag());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());

        if (e.getBooks() != null) {
            dto.setBookCount(e.getBooks().size());
        } else {
            dto.setBookCount(0);
        }

        return dto;
    }

    public static List<ShelfDTO> toDtoList(List<Shelf> list) {
        return list.stream().map(ShelfDTO::toDto).collect(Collectors.toList());
    }
}
