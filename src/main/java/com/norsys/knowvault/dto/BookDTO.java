package com.norsys.knowvault.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.norsys.knowvault.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO {
    private Long id;
    private String bookTitle;
    private String utilisateurLogin;
    private Long shelfId;
    private int pageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private List<ChapterDTO> chapters; //to receive the chapters


    public static BookDTO toDto(Book book) {
        if (book == null) return null;

        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setBookTitle(book.getBookTitle());

        if (book.getUtilisateurLogin() != null) {
            dto.setUtilisateurLogin(book.getUtilisateurLogin());
        }

        if (book.getShelf() != null) {
            dto.setShelfId(book.getShelf().getId());
        }

        int pageCount = 0;
        if (book.getChapters() != null) {
            dto.setChapters(ChapterDTO.toDtoList(book.getChapters()));
            for (var chapter : book.getChapters()) {
                if (chapter.getPages() != null) {
                    pageCount += chapter.getPages().size();
                }
            }
        }

        dto.setPageCount(pageCount);
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        dto.setDescription(book.getDescription());
        return dto;
    }

    public static List<BookDTO> toDtoList(List<Book> books) {
        return books.stream()
                .map(BookDTO::toDto)
                .collect(Collectors.toList());
    }
}
