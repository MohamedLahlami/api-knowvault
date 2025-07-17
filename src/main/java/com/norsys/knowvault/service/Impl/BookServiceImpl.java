package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.BookDTO;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.model.Utilisateur;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.BookService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ShelfRepository shelfRepository;
    @Override
    public BookDTO create(BookDTO dto) {

        Shelf shelf = shelfRepository.findById(dto.getShelfId())
                .orElseThrow(() -> new RuntimeException("Etagère introuvable"));

        Book book = Book.builder()
                .bookTitle(dto.getBookTitle())
                .utilisateurId(dto.getUtilisateurId())
                .shelf(shelf)
                .build();

        return BookDTO.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAllWithChapters();

        for (Book book : books) {
            for (Chapter chapter : book.getChapters()) {
                chapter.getPages().size();
            }
        }

        return BookDTO.toDtoList(books);
    }

    @Override
    public BookDTO findById(Long id) {
        return BookDTO.toDto(
                bookRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Livre introuvable"))
        );
    }

    @Override
    public BookDTO update(Long id, BookDTO dto) {
        Book livre = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre introuvable"));

        if (dto.getBookTitle() != null) livre.setBookTitle(dto.getBookTitle());

        if (dto.getUtilisateurId() != null) livre.setUtilisateurId(dto.getUtilisateurId());

        if (dto.getShelfId() != null) {
            Shelf e = shelfRepository.findById(dto.getShelfId())
                    .orElseThrow(() -> new RuntimeException("Etagère introuvable"));
            livre.setShelf(e);
        }

        return BookDTO.toDto(bookRepository.save(livre));
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Livre introuvable");
        }
        bookRepository.deleteById(id);
    }

}
