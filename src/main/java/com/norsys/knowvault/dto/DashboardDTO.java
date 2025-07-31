package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Shelf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private long totalShelves;
    private long totalBooks;
    private long totalPages;
    private List<BookDTO> recentBooks;
    private List<ShelfDTO> topShelves;

    public static DashboardDTO toDto(long totalShelves, long totalBooks, long totalPages,
                                     List<Book> recentBookEntities, List<Shelf> topShelfEntities) {
        DashboardDTO dto = new DashboardDTO();
        dto.setTotalShelves(totalShelves);
        dto.setTotalBooks(totalBooks);
        dto.setTotalPages(totalPages);

        dto.setRecentBooks(BookDTO.toDtoList(recentBookEntities));
        dto.setTopShelves(ShelfDTO.toDtoList(topShelfEntities));

        return dto;
    }
}
