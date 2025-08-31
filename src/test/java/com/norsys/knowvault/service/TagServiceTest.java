package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Tag;
import com.norsys.knowvault.repository.TagRepository;
import com.norsys.knowvault.service.Impl.TagServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag testTag;
    private TagDTO testTagDTO;

    @BeforeEach
    void setUp() {
        testTag = Tag.builder()
                .id(1L)
                .label("Technology")
                .type(TagType.BOOK)
                .build();

        testTagDTO = new TagDTO();
        testTagDTO.setId(1L);
        testTagDTO.setLabel("Technology");
        testTagDTO.setType(TagType.BOOK);
    }

    @Test
    void create_ShouldCreateTagSuccessfully() {
        // Given
        when(tagRepository.save(any(Tag.class))).thenReturn(testTag);

        // When
        TagDTO result = tagService.create(testTagDTO);

        // Then
        assertNotNull(result);
        assertEquals("Technology", result.getLabel());
        assertEquals(TagType.BOOK, result.getType());
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void findAll_ShouldReturnListOfTags() {
        // Given
        List<Tag> tags = List.of(testTag);
        when(tagRepository.findAll()).thenReturn(tags);

        // When
        List<TagDTO> result = tagService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Technology", result.get(0).getLabel());
        assertEquals(TagType.BOOK, result.get(0).getType());
        verify(tagRepository).findAll();
    }

    @Test
    void findById_ShouldReturnTag_WhenTagExists() {
        // Given
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));

        // When
        TagDTO result = tagService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Technology", result.getLabel());
        assertEquals(TagType.BOOK, result.getType());
        verify(tagRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenTagNotFound() {
        // Given
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> tagService.findById(1L));
        assertEquals("Tag not found with id: 1", exception.getMessage());
        verify(tagRepository).findById(1L);
    }

    @Test
    void findByTypeBook_ShouldReturnBookTags() {
        // Given
        List<Tag> bookTags = List.of(testTag);
        when(tagRepository.findByType(TagType.BOOK)).thenReturn(bookTags);

        // When
        List<TagDTO> result = tagService.findByTypeBook();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Technology", result.get(0).getLabel());
        assertEquals(TagType.BOOK, result.get(0).getType());
        verify(tagRepository).findByType(TagType.BOOK);
    }

    @Test
    void findByTypeShelf_ShouldReturnShelfTags() {
        // Given
        Tag shelfTag = Tag.builder()
                .id(2L)
                .label("Programming")
                .type(TagType.SHELF)
                .build();
        List<Tag> shelfTags = List.of(shelfTag);
        when(tagRepository.findByType(TagType.SHELF)).thenReturn(shelfTags);

        // When
        List<TagDTO> result = tagService.findByTypeShelf();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Programming", result.get(0).getLabel());
        assertEquals(TagType.SHELF, result.get(0).getType());
        verify(tagRepository).findByType(TagType.SHELF);
    }

    @Test
    void update_ShouldUpdateTagSuccessfully() {
        // Given
        TagDTO updateDTO = new TagDTO();
        updateDTO.setLabel("Updated Technology");
        updateDTO.setType(TagType.SHELF);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));
        when(tagRepository.save(any(Tag.class))).thenReturn(testTag);

        // When
        TagDTO result = tagService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(tagRepository).findById(1L);
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void update_ShouldThrowException_WhenTagNotFound() {
        // Given
        TagDTO updateDTO = new TagDTO();
        updateDTO.setLabel("Updated Technology");
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> tagService.update(1L, updateDTO));
        assertEquals("Tag not found with id: 1", exception.getMessage());
        verify(tagRepository).findById(1L);
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void delete_ShouldDeleteTagSuccessfully() {
        // Given
        when(tagRepository.existsById(1L)).thenReturn(true);

        // When
        tagService.delete(1L);

        // Then
        verify(tagRepository).existsById(1L);
        verify(tagRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenTagNotFound() {
        // Given
        when(tagRepository.existsById(1L)).thenReturn(false);

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> tagService.delete(1L));
        assertEquals("Tag not found with id: 1", exception.getMessage());
        verify(tagRepository).existsById(1L);
        verify(tagRepository, never()).deleteById(anyLong());
    }
}
