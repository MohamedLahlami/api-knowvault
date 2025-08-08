package com.norsys.knowvault.dto;

import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Shelf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DashboardDTO {
    private long totalShelves;
    private long totalBooks;
    private long totalPages;
    private List<BookDTO> recentBooks;
    private List<ShelfDTO> topShelves;
    private List<TagDTO> shelfTagStats;
    private List<TagDTO> bookTagStats;

    public DashboardDTO(long totalShelves, long totalBooks, long totalPages,
                        List<BookDTO> recentBooks, List<ShelfDTO> topShelves,
                        List<TagDTO> shelfTagStats, List<TagDTO> bookTagStats) {
        this.totalShelves = totalShelves;
        this.totalBooks = totalBooks;
        this.totalPages = totalPages;
        this.recentBooks = recentBooks;
        this.topShelves = topShelves;
        this.shelfTagStats = shelfTagStats;
        this.bookTagStats = bookTagStats;
    }

}

