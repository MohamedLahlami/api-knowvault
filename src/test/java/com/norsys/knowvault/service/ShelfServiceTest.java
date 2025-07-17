package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.enumerator.Tag;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.service.Impl.ShelfServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShelfServiceImplTest {

    private ShelfRepository shelfRepository;
    private ShelfServiceImpl shelfService;

    @BeforeEach
    void setUp() {
        shelfRepository = mock(ShelfRepository.class);
        shelfService = new ShelfServiceImpl(shelfRepository);
    }

    @Test
    void testCreate() {
        ShelfDTO dto = new ShelfDTO(null, "Shelf 1", "Description", Tag.JAVA);
        Shelf savedShelf = new Shelf(1L, "Shelf 1", "Description", Tag.JAVA, null);

        when(shelfRepository.save(any(Shelf.class))).thenReturn(savedShelf);

        ShelfDTO result = shelfService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Shelf 1", result.getLabel());
        assertEquals("Description", result.getDescription());
        assertEquals(Tag.JAVA, result.getTag());

        verify(shelfRepository, times(1)).save(any(Shelf.class));
    }

    @Test
    void testFindAll() {
        List<Shelf> shelves = Arrays.asList(
                new Shelf(1L, "A", "A desc", Tag.JAVA, null),
                new Shelf(2L, "B", "B desc", Tag.MOBILE, null)
        );
        when(shelfRepository.findAll()).thenReturn(shelves);

        List<ShelfDTO> result = shelfService.findAll();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getLabel());
        assertEquals(Tag.JAVA, result.get(0).getTag());
        assertEquals(Tag.MOBILE, result.get(1).getTag());
    }

    @Test
    void testFindById_found() {
        Shelf shelf = new Shelf(1L, "Shelf A", "Desc A", Tag.JAVA, null);
        when(shelfRepository.findById(1L)).thenReturn(Optional.of(shelf));

        ShelfDTO result = shelfService.findById(1L);

        assertNotNull(result);
        assertEquals("Shelf A", result.getLabel());
        assertEquals(Tag.JAVA, result.getTag());
    }

    @Test
    void testUpdate() {
        Shelf existing = new Shelf(1L, "Old Label", "Old Desc", Tag.JAVA, null);
        ShelfDTO dto = new ShelfDTO(null, "New Label", "New Desc", Tag.MOBILE);

        when(shelfRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(shelfRepository.save(any(Shelf.class))).thenReturn(existing);

        ShelfDTO result = shelfService.update(1L, dto);

        assertEquals("New Label", result.getLabel());
        assertEquals("New Desc", result.getDescription());
        assertEquals(Tag.MOBILE, result.getTag());
    }

    @Test
    void testDelete() {
        ShelfDTO dto = new ShelfDTO(2L, "Shelf 1", "Description", Tag.JAVA);
        Shelf savedShelf = new Shelf(1L, "Shelf 1", "Description", Tag.JAVA, null);

        shelfRepository.save(savedShelf);

        ShelfDTO result = shelfService.create(dto);

        doNothing().when(shelfRepository).deleteById(1L);

        shelfService.delete(1L);

        verify(shelfService, times(1)).delete(1L);
    }


}
