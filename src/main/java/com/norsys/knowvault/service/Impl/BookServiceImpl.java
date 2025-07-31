package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.BookDTO;
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
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ShelfRepository shelfRepository;
    private final TagService tagService;

    @Override
    public BookDTO create(BookDTO dto, Authentication authentication) {
        Shelf shelf = shelfRepository.findById(dto.getShelfId())
                .orElseThrow(() -> new RuntimeException("Étagère introuvable"));

        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        Jwt jwt = (Jwt) jwtAuth.getToken();
        String username = jwt.getClaim("preferred_username");

        Book book = Book.builder()
                .bookTitle(dto.getBookTitle())
                .utilisateurLogin(username)
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .shelf(shelf)
                .build();

        Book savedBook = bookRepository.save(book);


        if (dto.getTags() != null) {
            for (TagDTO tagDTO : dto.getTags()) {
                tagDTO.setResourceId(savedBook.getId());
                tagDTO.setType(TagType.BOOK);
                tagService.createTag(tagDTO);
            }
        }


        List<TagDTO> tags = tagService.getTagsByBookId(savedBook.getId());
        return BookDTO.toDto(savedBook, tags);
    }

    @Override
    public Page<BookDTO> findAll(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return booksPage.map(book -> {
            List<TagDTO> tags = tagService.getTagsByBookId(book.getId());
            return BookDTO.toDto(book, tags);
        });
    }

    @Override
    public Page<BookDTO> searchByTitle(String bookTitle, Pageable pageable) {
        Page<Book> booksPage = bookRepository.findByBookTitleContainingIgnoreCase(bookTitle, pageable);
        return booksPage.map(book -> {
            List<TagDTO> tags = tagService.getTagsByBookId(book.getId());
            return BookDTO.toDto(book, tags);
        });
    }

    @Override
    public BookDTO findById(Long id) {
        return BookDTO.toDto(
                bookRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Livre introuvable"))
        );
    }

    @Override
    public BookDTO findByIdWithTags(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre introuvable"));

        BookDTO dto = BookDTO.toDto(book);
        dto.setTags(tagService.getTagsByBookId(id));
        return dto;
    }

    @Override
    public BookDTO update(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre introuvable"));

        if (dto.getBookTitle() != null) book.setBookTitle(dto.getBookTitle());
        if (dto.getUtilisateurLogin() != null) book.setUtilisateurLogin(dto.getUtilisateurLogin());

        if (dto.getShelfId() != null) {
            Shelf shelf = shelfRepository.findById(dto.getShelfId())
                    .orElseThrow(() -> new RuntimeException("Étagère introuvable"));
            book.setShelf(shelf);
        }

        if (dto.getDescription() != null) book.setDescription(dto.getDescription());

        book.setUpdatedAt(LocalDateTime.now());

        // Gestion des tags
        if (dto.getTags() != null) {
            // Supprimer les anciens tags liés
            List<TagDTO> oldTags = tagService.getTagsByBookId(id);
            for (TagDTO oldTag : oldTags) {
                tagService.deleteTag(oldTag.getId());
            }

            // Créer les nouveaux tags
            for (TagDTO newTag : dto.getTags()) {
                newTag.setResourceId(id);
                newTag.setType(TagType.BOOK);
                tagService.createTag(newTag);
            }
        }

        Book savedBook = bookRepository.save(book);

        // Recharger les tags après mise à jour
        List<TagDTO> tags = tagService.getTagsByBookId(id);

        return BookDTO.toDto(savedBook, tags);
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Livre introuvable");
        }

        // Supprimer les tags associés avant de supprimer le livre
        List<TagDTO> tags = tagService.getTagsByBookId(id);
        for (TagDTO tag : tags) {
            tagService.deleteTag(tag.getId());
        }

        bookRepository.deleteById(id);
    }
}
