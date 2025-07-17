package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Book;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class BookDTO {
    private Long id;
    private String bookTitle;
    private UUID utilisateurId;
    private Long shelfId;
    private int pageCount;

    public static BookDTO toDto(Book book) {
        if (book == null) return null;

        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setBookTitle(book.getBookTitle());
        if (book.getUtilisateurId() != null) {
            dto.setUtilisateurId(book.getUtilisateurId());
        }
        if (book.getShelf() != null) {
            dto.setShelfId(book.getShelf().getId());
        }

        int pageCount = 0;
        if (book.getChapters() != null) {
            for (var chapter : book.getChapters()) {
                if (chapter.getPages() != null) {
                    pageCount += chapter.getPages().size();
                }
            }
        }

        dto.setPageCount(pageCount);
        return dto;
    }

    public static List<BookDTO> toDtoList(List<Book> books) {
        return books.stream()
                .map(BookDTO::toDto)
                .collect(Collectors.toList());
    }
}
