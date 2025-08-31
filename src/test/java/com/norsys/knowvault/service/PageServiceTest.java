package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.PageDTO;
import com.norsys.knowvault.enumerator.PageStatus;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.repository.ChapterRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.service.Impl.PageServiceImpl;
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
class PageServiceTest {

    @Mock
    private PageRepository pageRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private PageServiceImpl pageService;

    private Page testPage;
    private PageDTO testPageDTO;
    private Chapter testChapter;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = Book.builder()
                .id(1L)
                .bookTitle("Test Book")
                .build();

        testChapter = Chapter.builder()
                .id(1L)
                .chapterTitle("Test Chapter")
                .book(testBook)
                .build();

        testPage = Page.builder()
                .id(1L)
                .pageNumber(1)
                .content("Test page content")
                .markdownContent("# Test Markdown")
                .status(PageStatus.Draft)
                .chapter(testChapter)
                .build();

        testPageDTO = new PageDTO();
        testPageDTO.setId(1L);
        testPageDTO.setPageNumber(1);
        testPageDTO.setContent("Test page content");
        testPageDTO.setMarkDownContent("# Test Markdown");
        testPageDTO.setStatus(PageStatus.Draft);
        testPageDTO.setChapterId(1L);
    }

    @Test
    void create_ShouldCreatePageSuccessfully() {
        // Given
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(testChapter));
        when(pageRepository.save(any(Page.class))).thenReturn(testPage);

        // When
        PageDTO result = pageService.create(testPageDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPageNumber());
        assertEquals("Test page content", result.getContent());
        assertEquals("# Test Markdown", result.getMarkDownContent());
        assertEquals(PageStatus.Draft, result.getStatus());
        assertEquals(1L, result.getChapterId());
        verify(chapterRepository).findById(1L);
        verify(pageRepository).save(any(Page.class));
    }

    @Test
    void create_ShouldThrowException_WhenChapterNotFound() {
        // Given
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pageService.create(testPageDTO));
        assertEquals("Chapitre introuvable avec ID = 1", exception.getMessage());
        verify(chapterRepository).findById(1L);
        verify(pageRepository, never()).save(any(Page.class));
    }

    @Test
    void findAll_ShouldReturnListOfPages() {
        // Given
        List<Page> pages = List.of(testPage);
        when(pageRepository.findAll()).thenReturn(pages);

        // When
        List<PageDTO> result = pageService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test page content", result.get(0).getContent());
        verify(pageRepository).findAll();
    }

    @Test
    void findById_ShouldReturnPage_WhenPageExists() {
        // Given
        when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));

        // When
        PageDTO result = pageService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getPageNumber());
        assertEquals("Test page content", result.getContent());
        verify(pageRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenPageNotFound() {
        // Given
        when(pageRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pageService.findById(1L));
        assertEquals("Page introuvable avec ID = 1", exception.getMessage());
        verify(pageRepository).findById(1L);
    }

    @Test
    void update_ShouldUpdatePageNumberSuccessfully() {
        // Given
        PageDTO updateDTO = new PageDTO();
        updateDTO.setPageNumber(2);

        when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
        when(pageRepository.save(any(Page.class))).thenReturn(testPage);

        // When
        PageDTO result = pageService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(pageRepository).findById(1L);
        verify(pageRepository).save(any(Page.class));
    }

    @Test
    void update_ShouldUpdateContentSuccessfully() {
        // Given
        PageDTO updateDTO = new PageDTO();
        updateDTO.setContent("Updated content");
        updateDTO.setMarkDownContent("# Updated Markdown");

        when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
        when(pageRepository.save(any(Page.class))).thenReturn(testPage);

        // When
        PageDTO result = pageService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(pageRepository).findById(1L);
        verify(pageRepository).save(any(Page.class));
    }

    @Test
    void update_ShouldUpdateStatusSuccessfully() {
        // Given
        PageDTO updateDTO = new PageDTO();
        updateDTO.setStatus(PageStatus.Published);

        when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
        when(pageRepository.save(any(Page.class))).thenReturn(testPage);

        // When
        PageDTO result = pageService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(pageRepository).findById(1L);
        verify(pageRepository).save(any(Page.class));
    }

    @Test
    void update_ShouldUpdateChapterSuccessfully() {
        // Given
        Chapter newChapter = Chapter.builder().id(2L).chapterTitle("New Chapter").build();
        PageDTO updateDTO = new PageDTO();
        updateDTO.setChapterId(2L);

        when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
        when(chapterRepository.findById(2L)).thenReturn(Optional.of(newChapter));
        when(pageRepository.save(any(Page.class))).thenReturn(testPage);

        // When
        PageDTO result = pageService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(pageRepository).findById(1L);
        verify(chapterRepository).findById(2L);
        verify(pageRepository).save(any(Page.class));
    }

    @Test
    void update_ShouldThrowException_WhenPageNotFound() {
        // Given
        PageDTO updateDTO = new PageDTO();
        updateDTO.setContent("Updated content");
        when(pageRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pageService.update(1L, updateDTO));
        assertEquals("Page introuvable avec ID = 1", exception.getMessage());
        verify(pageRepository).findById(1L);
        verify(pageRepository, never()).save(any(Page.class));
    }

    @Test
    void update_ShouldThrowException_WhenChapterNotFound() {
        // Given
        PageDTO updateDTO = new PageDTO();
        updateDTO.setChapterId(999L);
        when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
        when(chapterRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pageService.update(1L, updateDTO));
        assertEquals("Chapitre introuvable avec ID = 999", exception.getMessage());
        verify(pageRepository).findById(1L);
        verify(chapterRepository).findById(999L);
        verify(pageRepository, never()).save(any(Page.class));
    }

    @Test
    void delete_ShouldDeletePageSuccessfully() {
        // Given
        when(pageRepository.existsById(1L)).thenReturn(true);

        // When
        pageService.delete(1L);

        // Then
        verify(pageRepository).existsById(1L);
        verify(pageRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenPageNotFound() {
        // Given
        when(pageRepository.existsById(1L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pageService.delete(1L));
        assertEquals("Page introuvable avec ID = 1", exception.getMessage());
        verify(pageRepository).existsById(1L);
        verify(pageRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByChapterId_ShouldReturnPagesForChapter() {
        // Given
        List<Page> pages = List.of(testPage);
        when(pageRepository.findByChapterId(1L)).thenReturn(pages);

        // When
        List<PageDTO> result = pageService.findByChapterId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test page content", result.get(0).getContent());
        assertEquals(1L, result.get(0).getChapterId());
        verify(pageRepository).findByChapterId(1L);
    }

    @Test
    void findByChapterId_ShouldReturnEmptyList_WhenNoPagesFound() {
        // Given
        when(pageRepository.findByChapterId(1L)).thenReturn(List.of());

        // When
        List<PageDTO> result = pageService.findByChapterId(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pageRepository).findByChapterId(1L);
    }
}
