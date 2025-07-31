package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.dto.DashboardDTO;
import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ShelfRepository shelfRepository;
    private final BookRepository bookRepository;
    private final PageRepository pageRepository;

    @Override
    public DashboardDTO getDashboardStatistics() {
        long totalShelves = shelfRepository.count();
        long totalBooks = bookRepository.count();
        long totalPages = pageRepository.count();

        List<BookDTO> recentBooks = BookDTO.toDtoList(
                bookRepository.findTop3ByOrderByUpdatedAtDesc()
        );

        List<ShelfDTO> topShelves = ShelfDTO.toDtoList(
                shelfRepository.findTop3ByBookCountDesc(PageRequest.of(0, 3)));

        return new DashboardDTO(totalShelves, totalBooks, totalPages, recentBooks, topShelves);
    }
}
