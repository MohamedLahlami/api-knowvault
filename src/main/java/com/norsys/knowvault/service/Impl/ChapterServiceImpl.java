package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.ChapterDTO;
import com.norsys.knowvault.model.Book;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.repository.BookRepository;
import com.norsys.knowvault.repository.ChapterRepository;
import com.norsys.knowvault.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final BookRepository bookRepository;

    @Override
    public ChapterDTO create(ChapterDTO dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Livre introuvable avec ID = " + dto.getBookId()));

        Chapter chapter = new Chapter();
        chapter.setChapterTitle(dto.getChapterTitle());
        chapter.setBook(book);

        chapter = chapterRepository.save(chapter);
        return ChapterDTO.toDto(chapter);
    }

    @Override
    public List<ChapterDTO> findAll() {
        return ChapterDTO.toDtoList(chapterRepository.findAll());
    }

    @Override
    public ChapterDTO findById(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapitre introuvable avec ID = " + id));
        return ChapterDTO.toDto(chapter);
    }

    @Override
    public ChapterDTO update(Long id, ChapterDTO dto) {
        Chapter chapitre = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapitre introuvable avec ID = " + id));

        if (dto.getChapterTitle() != null) {
            chapitre.setChapterTitle(dto.getChapterTitle());
        }

        if (dto.getBookId() != null) {
            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new RuntimeException("Livre introuvable avec ID = " + dto.getBookId()));
            chapitre.setBook(book);
        }

        chapitre = chapterRepository.save(chapitre);
        return ChapterDTO.toDto(chapitre);
    }

    @Override
    public void delete(Long id) {
        if (!chapterRepository.existsById(id)) {
            throw new RuntimeException("Chapitre introuvable avec ID = " + id);
        }
        chapterRepository.deleteById(id);
    }
}
