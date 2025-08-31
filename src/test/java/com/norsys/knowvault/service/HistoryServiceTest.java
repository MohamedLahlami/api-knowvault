package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.HistoryDTO;
import com.norsys.knowvault.enumerator.ModificationType;
import com.norsys.knowvault.model.History;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.repository.HistoryRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.service.Impl.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private PageRepository pageRepository;

    @InjectMocks
    private HistoryServiceImpl historyService;

    private History testHistory;
    private HistoryDTO testHistoryDTO;
    private Page testPage;

    @BeforeEach
    void setUp() {
        testPage = Page.builder()
                .id(1L)
                .pageNumber(1)
                .content("Test page content")
                .build();

        testHistory = History.builder()
                .id(1L)
                .modificationDate(OffsetDateTime.now())
                .modificationType(ModificationType.Creation)
                .page(testPage)
                .build();

        testHistoryDTO = new HistoryDTO();
        testHistoryDTO.setId(1L);
        testHistoryDTO.setModificationDate(OffsetDateTime.now());
        testHistoryDTO.setModificationType(ModificationType.Creation);
        testHistoryDTO.setPageId(1L);
    }

    @Test
    void create_ShouldCreateHistorySuccessfully() {
        // Given
        when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
        when(historyRepository.save(any(History.class))).thenReturn(testHistory);

        // When
        HistoryDTO result = historyService.create(testHistoryDTO);

        // Then
        assertNotNull(result);
        assertEquals(ModificationType.Creation, result.getModificationType());
        assertEquals(1L, result.getPageId());
        verify(pageRepository).findById(1L);
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void create_ShouldThrowException_WhenPageNotFound() {
        // Given
        when(pageRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> historyService.create(testHistoryDTO));
        assertEquals("Page introuvable", exception.getMessage());
        verify(pageRepository).findById(1L);
        verify(historyRepository, never()).save(any(History.class));
    }

    @Test
    void findAll_ShouldReturnListOfHistories() {
        // Given
        List<History> histories = List.of(testHistory);
        when(historyRepository.findAll()).thenReturn(histories);

        // When
        List<HistoryDTO> result = historyService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ModificationType.Creation, result.get(0).getModificationType());
        verify(historyRepository).findAll();
    }

    @Test
    void findById_ShouldReturnHistory_WhenHistoryExists() {
        // Given
        when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));

        // When
        HistoryDTO result = historyService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(ModificationType.Creation, result.getModificationType());
        verify(historyRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenHistoryNotFound() {
        // Given
        when(historyRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> historyService.findById(1L));
        assertEquals("Historiy introuvable", exception.getMessage());
        verify(historyRepository).findById(1L);
    }

    @Test
    void update_ShouldUpdateModificationTypeSuccessfully() {
        // Given
        HistoryDTO updateDTO = new HistoryDTO();
        updateDTO.setModificationType(ModificationType.Modification);

        when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));
        when(historyRepository.save(any(History.class))).thenReturn(testHistory);

        // When
        HistoryDTO result = historyService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(historyRepository).findById(1L);
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void update_ShouldUpdateModificationDateSuccessfully() {
        // Given
        OffsetDateTime newDate = OffsetDateTime.now().plusDays(1);
        HistoryDTO updateDTO = new HistoryDTO();
        updateDTO.setModificationDate(newDate);

        when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));
        when(historyRepository.save(any(History.class))).thenReturn(testHistory);

        // When
        HistoryDTO result = historyService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(historyRepository).findById(1L);
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void update_ShouldUpdatePageSuccessfully() {
        // Given
        Page newPage = Page.builder().id(2L).pageNumber(2).build();
        HistoryDTO updateDTO = new HistoryDTO();
        updateDTO.setPageId(2L);

        when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));
        when(pageRepository.findById(2L)).thenReturn(Optional.of(newPage));
        when(historyRepository.save(any(History.class))).thenReturn(testHistory);

        // When
        HistoryDTO result = historyService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(historyRepository).findById(1L);
        verify(pageRepository).findById(2L);
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void update_ShouldThrowException_WhenHistoryNotFound() {
        // Given
        HistoryDTO updateDTO = new HistoryDTO();
        updateDTO.setModificationType(ModificationType.Modification);
        when(historyRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> historyService.update(1L, updateDTO));
        assertEquals("Historiy introuvable", exception.getMessage());
        verify(historyRepository).findById(1L);
        verify(historyRepository, never()).save(any(History.class));
    }

    @Test
    void update_ShouldThrowException_WhenPageNotFound() {
        // Given
        HistoryDTO updateDTO = new HistoryDTO();
        updateDTO.setPageId(999L);
        when(historyRepository.findById(1L)).thenReturn(Optional.of(testHistory));
        when(pageRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> historyService.update(1L, updateDTO));
        assertEquals("Page introuvable", exception.getMessage());
        verify(historyRepository).findById(1L);
        verify(pageRepository).findById(999L);
        verify(historyRepository, never()).save(any(History.class));
    }

    @Test
    void delete_ShouldDeleteHistorySuccessfully() {
        // Given
        when(historyRepository.existsById(1L)).thenReturn(true);

        // When
        historyService.delete(1L);

        // Then
        verify(historyRepository).existsById(1L);
        verify(historyRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenHistoryNotFound() {
        // Given
        when(historyRepository.existsById(1L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> historyService.delete(1L));
        assertEquals("Historiy introuvable", exception.getMessage());
        verify(historyRepository).existsById(1L);
        verify(historyRepository, never()).deleteById(anyLong());
    }
}
