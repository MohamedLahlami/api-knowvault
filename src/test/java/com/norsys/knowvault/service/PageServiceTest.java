package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.PageDTO;
import com.norsys.knowvault.enumerator.PageStatus;
import com.norsys.knowvault.repository.ChapterRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.service.Impl.PageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.model.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PageServiceTest {

    private PageRepository pageRepository;
    private ChapterRepository chapterRepository;
    private PageServiceImpl pageService;

    @BeforeEach
    public void setUp() {
        pageRepository = Mockito.mock(PageRepository.class);
        chapterRepository = Mockito.mock(ChapterRepository.class);
        pageService = new PageServiceImpl(pageRepository, chapterRepository);
    }

    @Test
    public void testCreatePage() {
        PageDTO dto = new PageDTO();
        dto.setPageNumber(1);
        dto.setContent("Content");
        dto.setStatus(PageStatus.Draft);
        dto.setChapterId(1L);

        // Mock chapitre existant
        Chapter chapter = new Chapter();
        chapter.setId(1L);
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(chapterRepository.existsById(1L)).thenReturn(true);

        // Mock sauvegarde page (simule génération d'ID)
        when(pageRepository.save(any())).thenAnswer(invocation -> {
            Page page = invocation.getArgument(0);
            page.setId(100L);
            return page;
        });

        PageDTO result = pageService.create(dto);

        assertEquals(dto.getContent(), result.getContent());
        assertEquals(dto.getPageNumber(), result.getPageNumber());
        assertEquals(dto.getChapterId(), result.getChapterId());
        assertEquals(PageStatus.Draft, result.getStatus());

        verify(pageRepository, times(1)).save(any());
    }

    @Test
    public void testFindAllPages() {
        when(pageRepository.findAll()).thenReturn(Arrays.asList());

        List<PageDTO> result = pageService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindById() {
        var page = new com.norsys.knowvault.model.Page();
        page.setId(1L);
        page.setPageNumber(1);
        page.setContent("Content");
        page.setStatus(PageStatus.Published);

        when(pageRepository.findById(1L)).thenReturn(Optional.of(page));

        PageDTO result = pageService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Content", result.getContent());
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        when(pageRepository.existsById(id)).thenReturn(true);
        doNothing().when(pageRepository).deleteById(id);

        pageService.delete(id);

        verify(pageRepository, times(1)).deleteById(id);
    }
}
