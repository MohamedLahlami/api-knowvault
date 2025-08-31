package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Chapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDTO {
    private Long id;
    private String chapterTitle;
    private Long bookId;

    public static ChapterDTO toDto(Chapter chapter) {
        if (chapter == null) {
            return null;
        }

        ChapterDTO dto = new ChapterDTO();
        dto.setId(chapter.getId());
        dto.setChapterTitle(chapter.getChapterTitle());
        if (chapter.getBook() != null) {
            dto.setBookId(chapter.getBook().getId());
        }

        return dto;
    }

    public static List<ChapterDTO> toDtoList(List<Chapter> chapters) {
        return chapters.stream()
                .map(ChapterDTO::toDto)
                .collect(Collectors.toList());
    }

}
