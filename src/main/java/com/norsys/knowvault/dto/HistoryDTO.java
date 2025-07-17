package com.norsys.knowvault.dto;

import com.norsys.knowvault.enumerator.ModificationType;
import com.norsys.knowvault.model.History;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HistoryDTO {
    private Long id;
    private OffsetDateTime modificationDate;
    private ModificationType modificationType;
    private Long pageId;

    public static HistoryDTO toDto(History h) {
        if (h == null) return null;

        HistoryDTO dto = new HistoryDTO();
        dto.setId(h.getId());
        dto.setModificationDate(h.getModificationDate());
        dto.setModificationType(h.getModificationType());
        if (h.getPage() != null) {
            dto.setPageId(h.getPage().getId());
        }

        return dto;
    }

    public static List<HistoryDTO> toDtoList(List<History> list) {
        return list.stream().map(HistoryDTO::toDto).collect(Collectors.toList());
    }

}
