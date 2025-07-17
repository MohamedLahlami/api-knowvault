package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.ChapterDTO;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.ChapterRepository;
import com.norsys.knowvault.service.Impl.ChapterServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChapterServiceImplTest {

    private ChapterRepository chapterRepository;
    private BookRepository bookRepository;
    private ChapterServiceImpl chapterService;

    @BeforeEach
    void setUp() {
        chapterRepository = mock(ChapterRepository.class);
        bookRepository = mock(BookRepository.class);
        chapterService = new ChapterServiceImpl(chapterRepository, bookRepository);
    }

    @Test
    void testCreate() {
        ChapterDTO dto = new ChapterDTO(null, "Chapitre 1", 1L);
        Book book = new Book();
        book.setId(1L);

        Chapter savedChapter = new Chapter(1L, "Chapitre 1", book, null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(chapterRepository.save(any(Chapter.class))).thenReturn(savedChapter);

        ChapterDTO result = chapterService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Chapitre 1", result.getChapterTitle());
        assertEquals(1L, result.getBookId());

        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    @Test
    void testFindAll() {
        Book book = new Book();
        book.setId(1L);

        List<Chapter> chapters = Arrays.asList(
                new Chapter(1L, "Chapitre A", book, null),
                new Chapter(2L, "Chapitre B", book, null)
        );

        when(chapterRepository.findAll()).thenReturn(chapters);

        List<ChapterDTO> result = chapterService.findAll();

        assertEquals(2, result.size());
        assertEquals("Chapitre A", result.get(0).getChapterTitle());
        assertEquals("Chapitre B", result.get(1).getChapterTitle());
    }

    @Test
    void testFindById_found() {
        Book book = new Book();
        book.setId(1L);

        Chapter chapter = new Chapter(1L, "Chapitre A", book, null);
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        ChapterDTO result = chapterService.findById(1L);

        assertNotNull(result);
        assertEquals("Chapitre A", result.getChapterTitle());
        assertEquals(1L, result.getBookId());
    }

    @Test
    void testUpdate() {
        Book book = new Book();
        book.setId(2L);

        Chapter existing = new Chapter(1L, "Old Title", book, null);
        ChapterDTO dto = new ChapterDTO(null, "New Title", 2L);

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(chapterRepository.save(any(Chapter.class))).thenReturn(existing);

        ChapterDTO result = chapterService.update(1L, dto);

        assertEquals("New Title", result.getChapterTitle());
        assertEquals(2L, result.getBookId());
    }

    @Test
    void testDelete() {
        doNothing().when(chapterRepository).deleteById(1L);

        chapterService.delete(1L);

        verify(chapterRepository, times(1)).deleteById(1L);
    }
}
