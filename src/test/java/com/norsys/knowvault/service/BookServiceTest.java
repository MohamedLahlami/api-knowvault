package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.service.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ShelfRepository shelfRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookDTO testBookDTO;
    private Shelf testShelf;

    @BeforeEach
    void setUp() {
        testShelf = Shelf.builder()
                .id(1L)
                .label("Test Shelf")
                .description("Test shelf description")
                .build();

        testBook = Book.builder()
                .id(1L)
                .bookTitle("Test Book")
                .utilisateurLogin("testuser")
                .description("Test book description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .shelf(testShelf)
                .build();

        testBookDTO = new BookDTO();
        testBookDTO.setId(1L);
        testBookDTO.setBookTitle("Test Book");
        testBookDTO.setUtilisateurLogin("testuser");
        testBookDTO.setDescription("Test book description");
        testBookDTO.setShelfId(1L);
    }

    @Test
    void create_ShouldCreateBookSuccessfully() {
        // Given
        when(shelfRepository.findById(1L)).thenReturn(Optional.of(testShelf));
        when(jwt.getClaim("preferred_username")).thenReturn("testuser");
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);

        // When
        BookDTO result = bookService.create(testBookDTO, jwtAuth);

        // Then
        assertNotNull(result);
        assertEquals("Test Book", result.getBookTitle());
        assertEquals("testuser", result.getUtilisateurLogin());
        assertEquals(1L, result.getShelfId());
        verify(shelfRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void create_ShouldThrowException_WhenShelfNotFound() {
        // Given
        when(shelfRepository.findById(1L)).thenReturn(Optional.empty());

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookService.create(testBookDTO, jwtAuth));
        assertEquals("Étagère introuvable", exception.getMessage());
        verify(shelfRepository).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void findAll_ShouldReturnPageOfBooks() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(testBook);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        // When
        Page<BookDTO> result = bookService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getBookTitle());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void findAllBooks_ShouldReturnListOfBooks() {
        // Given
        List<Book> books = List.of(testBook);
        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<BookDTO> result = bookService.findAllBooks();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getBookTitle());
        verify(bookRepository).findAll();
    }

    @Test
    void findById_ShouldReturnBook_WhenBookExists() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // When
        BookDTO result = bookService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Book", result.getBookTitle());
        verify(bookRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenBookNotFound() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookService.findById(1L));
        assertEquals("Livre introuvable", exception.getMessage());
        verify(bookRepository).findById(1L);
    }

    @Test
    void searchByTitle_ShouldReturnMatchingBooks() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(testBook);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 1);
        when(bookRepository.findByBookTitleContainingIgnoreCase("Test", pageable))
            .thenReturn(bookPage);

        // When
        Page<BookDTO> result = bookService.searchByTitle("Test", pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getBookTitle());
        verify(bookRepository).findByBookTitleContainingIgnoreCase("Test", pageable);
    }

    @Test
    void update_ShouldUpdateBookSuccessfully() {
        // Given
        BookDTO updateDTO = new BookDTO();
        updateDTO.setBookTitle("Updated Book");
        updateDTO.setDescription("Updated description");
        updateDTO.setShelfId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(shelfRepository.findById(1L)).thenReturn(Optional.of(testShelf));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        BookDTO result = bookService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(bookRepository).findById(1L);
        verify(shelfRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void update_ShouldThrowException_WhenBookNotFound() {
        // Given
        BookDTO updateDTO = new BookDTO();
        updateDTO.setBookTitle("Updated Book");
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookService.update(1L, updateDTO));
        assertEquals("Livre introuvable", exception.getMessage());
        verify(bookRepository).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void update_ShouldThrowException_WhenShelfNotFound() {
        // Given
        BookDTO updateDTO = new BookDTO();
        updateDTO.setShelfId(999L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(shelfRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookService.update(1L, updateDTO));
        assertEquals("Étagère introuvable", exception.getMessage());
        verify(bookRepository).findById(1L);
        verify(shelfRepository).findById(999L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void delete_ShouldDeleteBookSuccessfully() {
        // Given
        when(bookRepository.existsById(1L)).thenReturn(true);

        // When
        bookService.delete(1L);

        // Then
        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenBookNotFound() {
        // Given
        when(bookRepository.existsById(1L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookService.delete(1L));
        assertEquals("Livre introuvable", exception.getMessage());
        verify(bookRepository).existsById(1L);
        verify(bookRepository, never()).deleteById(anyLong());
    }
}
