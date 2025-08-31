package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.ChapterDTO;
import com.norsys.knowvault.exception.DuplicateChapterException;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.ChapterRepository;
import com.norsys.knowvault.service.Impl.ChapterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChapterServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ChapterServiceImpl chapterService;

    private Chapter testChapter;
    private ChapterDTO testChapterDTO;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = Book.builder()
                .id(1L)
                .bookTitle("Test Book")
                .utilisateurLogin("testuser")
                .build();

        testChapter = Chapter.builder()
                .id(1L)
                .chapterTitle("Test Chapter")
                .book(testBook)
                .build();

        testChapterDTO = new ChapterDTO();
        testChapterDTO.setId(1L);
        testChapterDTO.setChapterTitle("Test Chapter");
        testChapterDTO.setBookId(1L);
    }

    @Test
    void create_ShouldCreateChapterSuccessfully() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(chapterRepository.existsByChapterTitleAndBook("Test Chapter", testBook)).thenReturn(false);
        when(chapterRepository.save(any(Chapter.class))).thenReturn(testChapter);

        // When
        ChapterDTO result = chapterService.create(testChapterDTO);

        // Then
        assertNotNull(result);
        assertEquals("Test Chapter", result.getChapterTitle());
        assertEquals(1L, result.getBookId());
        verify(bookRepository).findById(1L);
        verify(chapterRepository).existsByChapterTitleAndBook("Test Chapter", testBook);
        verify(chapterRepository).save(any(Chapter.class));
    }

    @Test
    void create_ShouldThrowException_WhenBookNotFound() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chapterService.create(testChapterDTO));
        assertEquals("Livre introuvable avec ID = 1", exception.getMessage());
        verify(bookRepository).findById(1L);
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    @Test
    void create_ShouldThrowException_WhenDuplicateChapterTitle() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(chapterRepository.existsByChapterTitleAndBook("Test Chapter", testBook)).thenReturn(true);

        // When & Then
        DuplicateChapterException exception = assertThrows(DuplicateChapterException.class,
                () -> chapterService.create(testChapterDTO));
        assertEquals("Un chapitre avec ce nom existe déjà pour ce livre.", exception.getMessage());
        verify(bookRepository).findById(1L);
        verify(chapterRepository).existsByChapterTitleAndBook("Test Chapter", testBook);
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    @Test
    void findAll_ShouldReturnListOfChapters() {
        // Given
        List<Chapter> chapters = List.of(testChapter);
        when(chapterRepository.findAll()).thenReturn(chapters);

        // When
        List<ChapterDTO> result = chapterService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Chapter", result.get(0).getChapterTitle());
        verify(chapterRepository).findAll();
    }

    @Test
    void findById_ShouldReturnChapter_WhenChapterExists() {
        // Given
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(testChapter));

        // When
        ChapterDTO result = chapterService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Chapter", result.getChapterTitle());
        verify(chapterRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenChapterNotFound() {
        // Given
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chapterService.findById(1L));
        assertEquals("Chapitre introuvable avec ID = 1", exception.getMessage());
        verify(chapterRepository).findById(1L);
    }

    @Test
    void update_ShouldUpdateChapterTitleSuccessfully() {
        // Given
        ChapterDTO updateDTO = new ChapterDTO();
        updateDTO.setChapterTitle("Updated Chapter");

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(testChapter));
        when(chapterRepository.save(any(Chapter.class))).thenReturn(testChapter);

        // When
        ChapterDTO result = chapterService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(chapterRepository).findById(1L);
        verify(chapterRepository).save(any(Chapter.class));
    }

    @Test
    void update_ShouldUpdateBookSuccessfully() {
        // Given
        Book newBook = Book.builder().id(2L).bookTitle("New Book").build();
        ChapterDTO updateDTO = new ChapterDTO();
        updateDTO.setBookId(2L);

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(testChapter));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(newBook));
        when(chapterRepository.save(any(Chapter.class))).thenReturn(testChapter);

        // When
        ChapterDTO result = chapterService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(chapterRepository).findById(1L);
        verify(bookRepository).findById(2L);
        verify(chapterRepository).save(any(Chapter.class));
    }

    @Test
    void update_ShouldThrowException_WhenChapterNotFound() {
        // Given
        ChapterDTO updateDTO = new ChapterDTO();
        updateDTO.setChapterTitle("Updated Chapter");
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chapterService.update(1L, updateDTO));
        assertEquals("Chapitre introuvable avec ID = 1", exception.getMessage());
        verify(chapterRepository).findById(1L);
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    @Test
    void update_ShouldThrowException_WhenBookNotFound() {
        // Given
        ChapterDTO updateDTO = new ChapterDTO();
        updateDTO.setBookId(999L);
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(testChapter));
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chapterService.update(1L, updateDTO));
        assertEquals("Livre introuvable avec ID = 999", exception.getMessage());
        verify(chapterRepository).findById(1L);
        verify(bookRepository).findById(999L);
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    @Test
    void delete_ShouldDeleteChapterSuccessfully() {
        // Given
        when(chapterRepository.existsById(1L)).thenReturn(true);

        // When
        chapterService.delete(1L);

        // Then
        verify(chapterRepository).existsById(1L);
        verify(chapterRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenChapterNotFound() {
        // Given
        when(chapterRepository.existsById(1L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chapterService.delete(1L));
        assertEquals("Chapitre introuvable avec ID = 1", exception.getMessage());
        verify(chapterRepository).existsById(1L);
        verify(chapterRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByBookId_ShouldReturnChaptersForBook() {
        // Given
        List<Chapter> chapters = List.of(testChapter);
        when(chapterRepository.findByBookId(1L)).thenReturn(chapters);

        // When
        List<ChapterDTO> result = chapterService.findByBookId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Chapter", result.get(0).getChapterTitle());
        assertEquals(1L, result.get(0).getBookId());
        verify(chapterRepository).findByBookId(1L);
    }

    @Test
    void findByBookId_ShouldReturnEmptyList_WhenNoChaptersFound() {
        // Given
        when(chapterRepository.findByBookId(1L)).thenReturn(List.of());

        // When
        List<ChapterDTO> result = chapterService.findByBookId(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(chapterRepository).findByBookId(1L);
    }
}
