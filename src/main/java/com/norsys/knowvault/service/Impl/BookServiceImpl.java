package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.BookService;
import com.norsys.knowvault.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ShelfRepository shelfRepository;

    @Override
    public BookDTO create(BookDTO dto, Authentication authentication) {
        log.debug("Creating book '{}' for shelf ID: {}", dto.getBookTitle(), dto.getShelfId());

        Shelf shelf = shelfRepository.findById(dto.getShelfId())
                .orElseThrow(() -> {
                    log.error("Shelf not found with ID: {}", dto.getShelfId());
                    return new RuntimeException("Étagère introuvable");
                });

        log.debug("Found shelf: '{}' for book creation", shelf.getLabel());

        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = (Jwt) jwtAuth.getToken();
        String username = jwt.getClaim("preferred_username");

        log.debug("Creating book for user: {}", username);

        Book book = Book.builder()
                .bookTitle(dto.getBookTitle())
                .utilisateurLogin(username)
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .shelf(shelf)
                .build();

        Book savedBook = bookRepository.save(book);
        log.info("Successfully created book '{}' with ID: {} for user: {}",
                savedBook.getBookTitle(), savedBook.getId(), username);

        return BookDTO.toDto(savedBook);
    }

    @Override
    public Page<BookDTO> findAll(Pageable pageable) {
        log.debug("Fetching books with pagination - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Book> booksPage = bookRepository.findAll(pageable);
        log.info("Found {} books on page {} of {}",
                booksPage.getNumberOfElements(), booksPage.getNumber() + 1, booksPage.getTotalPages());

        return booksPage.map(BookDTO::toDto);
    }

    @Override
    public List<BookDTO> findAllBooks() {
        log.debug("Fetching all books from database");
        List<Book> books = bookRepository.findAll();
        log.info("Found {} books in database", books.size());

        return books.stream()
                .map(BookDTO::toDto)
                .toList();
    }

    @Override
    public Page<BookDTO> searchByTitle(String bookTitle, Pageable pageable) {
        log.debug("Searching books by title containing: '{}', page: {}, size: {}",
                bookTitle, pageable.getPageNumber(), pageable.getPageSize());

        Page<Book> booksPage = bookRepository.findByBookTitleContainingIgnoreCase(bookTitle, pageable);
        log.info("Search for '{}' found {} books", bookTitle, booksPage.getNumberOfElements());

        return booksPage.map(BookDTO::toDto);
    }

    @Override
    public BookDTO findById(Long id) {
        log.debug("Searching for book with ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with ID: {}", id);
                    return new RuntimeException("Livre introuvable");
                });

        log.debug("Successfully found book: '{}' (ID: {})", book.getBookTitle(), id);
        return BookDTO.toDto(book);
    }

    @Override
    public BookDTO update(Long id, BookDTO dto) {
        log.debug("Updating book with ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found for update with ID: {}", id);
                    return new RuntimeException("Livre introuvable");
                });

        log.debug("Found existing book: '{}' for update", book.getBookTitle());

        if (dto.getBookTitle() != null) {
            log.debug("Updating book title from '{}' to '{}'", book.getBookTitle(), dto.getBookTitle());
            book.setBookTitle(dto.getBookTitle());
        }
        if (dto.getUtilisateurLogin() != null) {
            log.debug("Updating book user from '{}' to '{}'", book.getUtilisateurLogin(), dto.getUtilisateurLogin());
            book.setUtilisateurLogin(dto.getUtilisateurLogin());
        }

        if (dto.getShelfId() != null) {
            log.debug("Updating book shelf to ID: {}", dto.getShelfId());
            Shelf shelf = shelfRepository.findById(dto.getShelfId())
                    .orElseThrow(() -> {
                        log.error("Shelf not found for book update with ID: {}", dto.getShelfId());
                        return new RuntimeException("Étagère introuvable");
                    });
            book.setShelf(shelf);
        }

        if (dto.getDescription() != null) {
            log.debug("Updating book description (length: {})", dto.getDescription().length());
            book.setDescription(dto.getDescription());
        }

        book.setUpdatedAt(LocalDateTime.now());

        Book savedBook = bookRepository.save(book);
        log.info("Successfully updated book with ID: {}", id);

        return BookDTO.toDto(savedBook);
    }

    @Override
    public void delete(Long id) {
        log.debug("Attempting to delete book with ID: {}", id);

        if (!bookRepository.existsById(id)) {
            log.error("Cannot delete book - not found with ID: {}", id);
            throw new RuntimeException("Livre introuvable");
        }

        bookRepository.deleteById(id);
        log.info("Successfully deleted book with ID: {}", id);
    }
}