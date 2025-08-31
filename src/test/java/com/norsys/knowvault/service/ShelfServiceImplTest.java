package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.service.Impl.ShelfServiceImpl;
import com.norsys.knowvault.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelfServiceImplTest {

    @Mock
    private ShelfRepository shelfRepository;

    @Mock
    private TagService tagService;

    private ShelfServiceImpl shelfService;

    private Shelf testShelf;
    private ShelfDTO testShelfDTO;
    private TagDTO testTagDTO;

    @BeforeEach
    void setUp() {
        // Fix: Use the correct constructor with both dependencies
        shelfService = new ShelfServiceImpl(shelfRepository, tagService);

        testTagDTO = new TagDTO();
        testTagDTO.setId(1L);
        testTagDTO.setLabel("Test Tag");

        testShelf = Shelf.builder()
                .id(1L)
                .label("Test Shelf")
                .description("Test shelf description")
                .imageName("test.png")
                .views(0)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        testShelfDTO = new ShelfDTO();
        testShelfDTO.setId(1L);
        testShelfDTO.setLabel("Test Shelf");
        testShelfDTO.setDescription("Test shelf description");
        testShelfDTO.setImageName("test.png");
        testShelfDTO.setTag(testTagDTO);
    }

    @Test
    void testCreate() {
        // Given
        when(tagService.findById(1L)).thenReturn(testTagDTO);
        when(shelfRepository.save(any(Shelf.class))).thenReturn(testShelf);

        // When
        ShelfDTO result = shelfService.create(testShelfDTO);

        // Then
        assertNotNull(result);
        assertEquals("Test Shelf", result.getLabel());
        assertEquals("Test shelf description", result.getDescription());
        verify(shelfRepository).save(any(Shelf.class));
        verify(tagService).findById(1L);
    }

    @Test
    void testFindAll() {
        // Given
        List<Shelf> shelves = Arrays.asList(testShelf);
        when(shelfRepository.findAll()).thenReturn(shelves);

        // When
        List<ShelfDTO> result = shelfService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Shelf", result.get(0).getLabel());
        verify(shelfRepository).findAll();
    }

    @Test
    void testFindById_found() {
        // Given
        when(shelfRepository.findById(1L)).thenReturn(Optional.of(testShelf));

        // When
        ShelfDTO result = shelfService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Test Shelf", result.getLabel());
        verify(shelfRepository).findById(1L);
    }

    @Test
    void testFindById_notFound() {
        // Given
        when(shelfRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> shelfService.findById(1L));
        verify(shelfRepository).findById(1L);
    }

    @Test
    void testUpdate() {
        // Given
        when(shelfRepository.findById(1L)).thenReturn(Optional.of(testShelf));
        when(tagService.findById(1L)).thenReturn(testTagDTO);
        when(shelfRepository.save(any(Shelf.class))).thenReturn(testShelf);

        ShelfDTO updateDTO = new ShelfDTO();
        updateDTO.setLabel("Updated Shelf");
        updateDTO.setDescription("Updated description");
        updateDTO.setTag(testTagDTO);

        // When
        ShelfDTO result = shelfService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(shelfRepository).findById(1L);
        verify(shelfRepository).save(any(Shelf.class));
        verify(tagService).findById(1L);
    }

    @Test
    void testDelete() {
        // Given
        when(shelfRepository.existsById(1L)).thenReturn(true);

        // When
        shelfService.delete(1L);

        // Then
        verify(shelfRepository).existsById(1L);
        verify(shelfRepository).deleteById(1L);
    }

    @Test
    void testFindAllPaginated() {
        // Given
        List<Shelf> shelves = Arrays.asList(testShelf);
        Page<Shelf> shelfPage = new PageImpl<>(shelves, PageRequest.of(0, 10), 1);
        when(shelfRepository.findAll(any(PageRequest.class))).thenReturn(shelfPage);

        // When
        Page<ShelfDTO> result = shelfService.findAllPaginated(0, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Shelf", result.getContent().get(0).getLabel());
        verify(shelfRepository).findAll(any(PageRequest.class));
    }
}
