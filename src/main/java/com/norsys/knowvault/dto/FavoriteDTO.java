package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Favorite;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class FavoriteDTO {
    private Long id;
    private UUID userId;
    private Long pageId;
    private Integer pageNumber;
    private String chapterTitle;
    private String bookTitle;


    public static FavoriteDTO toDto(Favorite f) {
        if (f == null) {
            return null;
        }

        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(f.getId());
        if (f.getUser() != null) {
            dto.setUserId(f.getUser().getId());
        }
        if (f.getPage() != null) {
            dto.setPageId(f.getPage().getId());
        }
        return dto;
    }

    public static List<FavoriteDTO> toDtoList(List<Favorite> list) {
        return list.stream().map(FavoriteDTO::toDto).collect(Collectors.toList());
    }

}
