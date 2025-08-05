package com.norsys.knowvault.repository;

import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    boolean existsByChapterTitleAndBook(String chapterTitle, Book book);
    List<Chapter> findByBookId(Long bookId);

}
