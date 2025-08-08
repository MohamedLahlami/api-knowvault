package com.norsys.knowvault.repository;

import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.chapters")
    List<Book> findAllWithChapters();

    Page<Book> findByBookTitleContainingIgnoreCase(String bookTitle, Pageable pageable);

    Page<Book> findAll(Pageable pageable);

    List<Book> findTop3ByOrderByUpdatedAtDesc();

    @Query("SELECT new com.norsys.knowvault.dto.TagDTO(t.id, t.label, t.type, COUNT(b)) " +
            "FROM Book b JOIN b.tag t GROUP BY t.id, t.label, t.type")
    List<TagDTO> countBooksByTag();
}
