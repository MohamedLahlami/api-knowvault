package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO {
    private Long id;
    private String bookTitle;
    private UUID utilisateurId;
    private Long shelfId;

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

        return dto;
    }

    public static List<BookDTO> toDtoList(List<Book> books) {
        return books.stream()
                .map(BookDTO::toDto)
                .collect(Collectors.toList());
    }
}
