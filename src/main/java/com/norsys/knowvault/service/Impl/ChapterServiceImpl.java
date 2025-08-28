package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.ChapterDTO;
import com.norsys.knowvault.exception.DuplicateChapterException;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.ChapterRepository;
import com.norsys.knowvault.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final BookRepository bookRepository;

    @Override
    public ChapterDTO create(ChapterDTO dto) {
        log.debug("Creating chapter '{}' for book ID: {}", dto.getChapterTitle(), dto.getBookId());

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> {
                    log.error("Book not found with ID: {}", dto.getBookId());
                    return new RuntimeException("Livre introuvable avec ID = " + dto.getBookId());
                });

        log.debug("Found book: '{}' for chapter creation", book.getBookTitle());

        boolean exists = chapterRepository.existsByChapterTitleAndBook(dto.getChapterTitle(), book);
        if (exists) {
            log.error("Duplicate chapter title '{}' found for book ID: {}", dto.getChapterTitle(), dto.getBookId());
            throw new DuplicateChapterException("Un chapitre avec ce nom existe déjà pour ce livre.");
        }

        Chapter chapter = new Chapter();
        chapter.setChapterTitle(dto.getChapterTitle());
        chapter.setBook(book);

        chapter = chapterRepository.save(chapter);
        log.info("Successfully created chapter '{}' with ID: {} for book: '{}'",
                chapter.getChapterTitle(), chapter.getId(), book.getBookTitle());

        return ChapterDTO.toDto(chapter);
    }

    @Override
    public List<ChapterDTO> findAll() {
        log.debug("Fetching all chapters from database");
        List<Chapter> chapters = chapterRepository.findAll();
        log.info("Found {} chapters in database", chapters.size());
        return ChapterDTO.toDtoList(chapters);
    }

    @Override
    public ChapterDTO findById(Long id) {
        log.debug("Searching for chapter with ID: {}", id);
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Chapter not found with ID: {}", id);
                    return new RuntimeException("Chapitre introuvable avec ID = " + id);
                });
        log.debug("Successfully found chapter: '{}' (ID: {})", chapter.getChapterTitle(), id);
        return ChapterDTO.toDto(chapter);
    }

    @Override
    public ChapterDTO update(Long id, ChapterDTO dto) {
        log.debug("Updating chapter with ID: {}", id);

        Chapter chapitre = chapterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Chapter not found for update with ID: {}", id);
                    return new RuntimeException("Chapitre introuvable avec ID = " + id);
                });

        log.debug("Found existing chapter: '{}' for update", chapitre.getChapterTitle());

        if (dto.getChapterTitle() != null) {
            log.debug("Updating chapter title from '{}' to '{}'", chapitre.getChapterTitle(), dto.getChapterTitle());
            chapitre.setChapterTitle(dto.getChapterTitle());
        }

        if (dto.getBookId() != null) {
            log.debug("Updating chapter book to ID: {}", dto.getBookId());
            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> {
                        log.error("Book not found for chapter update with ID: {}", dto.getBookId());
                        return new RuntimeException("Livre introuvable avec ID = " + dto.getBookId());
                    });
            chapitre.setBook(book);
        }

        chapitre = chapterRepository.save(chapitre);
        log.info("Successfully updated chapter with ID: {}", id);
        return ChapterDTO.toDto(chapitre);
    }

    @Override
    public void delete(Long id) {
        log.debug("Attempting to delete chapter with ID: {}", id);

        if (!chapterRepository.existsById(id)) {
            log.error("Cannot delete chapter - not found with ID: {}", id);
            throw new RuntimeException("Chapitre introuvable avec ID = " + id);
        }

        chapterRepository.deleteById(id);
        log.info("Successfully deleted chapter with ID: {}", id);
    }

    @Override
    public List<ChapterDTO> findByBookId(Long bookId) {
        log.debug("Fetching chapters for book ID: {}", bookId);

        List<Chapter> chapters = chapterRepository.findByBookId(bookId);
        log.info("Found {} chapters for book ID: {}", chapters.size(), bookId);

        return chapters.stream()
                .map(chapter -> {
                    ChapterDTO dto = new ChapterDTO();
                    dto.setId(chapter.getId());
                    dto.setChapterTitle(chapter.getChapterTitle());
                    dto.setBookId(chapter.getBook().getId()); // only if Book is not null
                    return dto;
                })
                .toList();
    }
}
