package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.service.Impl.BookServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private BookRepository bookRepository;
    private UtilisateurRepository utilisateurRepository;
    private ShelfRepository shelfRepository;
    private BookServiceImpl bookService;

    private final UUID testUserId = UUID.fromString("74aa141f-920c-4997-b41d-5aa5e8f72319");

    @BeforeEach
    public void setUp() {
        bookRepository = mock(BookRepository.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        shelfRepository = mock(ShelfRepository.class);
        //bookService = new BookServiceImpl(bookRepository, utilisateurRepository, shelfRepository);
    }

    /*@Test
    public void testCreate() {
        BookDTO dto = new BookDTO();
        dto.setId(null);
        dto.setBookTitle("Title 1");
        dto.setUtilisateurId(testUserId);
        dto.setShelfId(1L);

        Shelf shelf = new Shelf();
        shelf.setId(1L);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setBookTitle("Title 1");
        savedBook.setUtilisateurId(testUserId);
        savedBook.setShelf(shelf);

        when(shelfRepository.findById(1L)).thenReturn(Optional.of(shelf));
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookDTO result = bookService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Title 1", result.getBookTitle());
        assertEquals(testUserId, result.getUtilisateurId());
        assertEquals(1L, result.getShelfId());

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testFindAll() {
        Shelf shelf1 = new Shelf();
        shelf1.setId(1L);

        Shelf shelf2 = new Shelf();
        shelf2.setId(2L);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setBookTitle("Title A");
        book1.setUtilisateurId(testUserId);
        book1.setShelf(shelf1);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setBookTitle("Title B");
        book2.setUtilisateurId(testUserId);
        book2.setShelf(shelf2);

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<BookDTO> result = bookService.findAll();

        assertEquals(2, result.size());
        assertEquals("Title A", result.get(0).getBookTitle());
        assertEquals(testUserId, result.get(0).getUtilisateurId());
        assertEquals(1L, result.get(0).getShelfId());
        assertEquals("Title B", result.get(1).getBookTitle());
        assertEquals(2L, result.get(1).getShelfId());
    }

    @Test
    public void testFindById_found() {
        Shelf shelf = new Shelf();
        shelf.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setBookTitle("Title A");
        book.setUtilisateurId(testUserId);
        book.setShelf(shelf);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = bookService.findById(1L);

        assertNotNull(result);
        assertEquals("Title A", result.getBookTitle());
        assertEquals(testUserId, result.getUtilisateurId());
        assertEquals(1L, result.getShelfId());
    }

    @Test
    public void testUpdate() {
        Shelf oldShelf = new Shelf();
        oldShelf.setId(1L);

        Shelf newShelf = new Shelf();
        newShelf.setId(2L);

        Book existing = new Book();
        existing.setId(1L);
        existing.setBookTitle("Old Title");
        existing.setUtilisateurId(testUserId);
        existing.setShelf(oldShelf);

        BookDTO dto = new BookDTO();
        dto.setBookTitle("New Title");
        dto.setUtilisateurId(testUserId);
        dto.setShelfId(2L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(shelfRepository.findById(2L)).thenReturn(Optional.of(newShelf));
        when(bookRepository.save(any(Book.class))).thenReturn(existing);

        BookDTO result = bookService.update(1L, dto);

        assertEquals("New Title", result.getBookTitle());
        assertEquals(testUserId, result.getUtilisateurId());
        assertEquals(2L, result.getShelfId());
    }

    @Test
    void testDelete() {
        Long id = 1L;

        when(bookRepository.findById(id))
                .thenReturn(Optional.of(new Book()));

        when(bookRepository.existsById(id)).thenReturn(true);

        bookService.delete(id);

        verify(bookRepository, times(1)).deleteById(id);
    }*/
}
