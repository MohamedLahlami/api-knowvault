package com.norsys.knowvault.dto;

import com.norsys.knowvault.enumerator.PageStatus;
import com.norsys.knowvault.model.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO {
    private Long id;
    private Integer pageNumber;
    private String content;
    private String markDownContent;
    private PageStatus status;
    private Long chapterId;

    public static PageDTO toDto(Page page) {
        if (page == null) return null;

        PageDTO dto = new PageDTO();
        dto.setId(page.getId());
        dto.setPageNumber(page.getPageNumber());
        dto.setContent(page.getContent());
        dto.setMarkDownContent(page.getMarkdownContent());
        dto.setStatus(page.getStatus());

        if (page.getChapter() != null) {
            dto.setChapterId(page.getChapter().getId());
        }

        return dto;
    }

    public static List<PageDTO> toDtoList(List<Page> pages) {
        return pages.stream()
                .map(PageDTO::toDto)
                .collect(Collectors.toList());
    }



}
