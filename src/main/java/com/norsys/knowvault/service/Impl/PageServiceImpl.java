package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.PageDTO;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.repository.ChapterRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public PageDTO create(PageDTO dto) {
        Chapter chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapitre introuvable avec ID = "
                        + dto.getChapterId()));

        Page page = new Page();
        page.setPageNumber(dto.getPageNumber());
        page.setContent(dto.getContent());
        page.setMarkdownContent(dto.getMarkDownContent());
        page.setStatus(dto.getStatus());
        page.setChapter(chapter);

        page = pageRepository.save(page);
        return PageDTO.toDto(page);
    }

    @Override
    public List<PageDTO> findAll() {
        return PageDTO.toDtoList(pageRepository.findAll());
    }

    @Override
    public PageDTO findById(Long id) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page introuvable avec ID = " + id));
        return PageDTO.toDto(page);
    }

    @Override
    public PageDTO update(Long id, PageDTO dto) {
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page introuvable avec ID = " + id));

        if (dto.getPageNumber() != null) {
            page.setPageNumber(dto.getPageNumber());
        }
        if (dto.getContent() != null) {
            page.setContent(dto.getContent());
        }
        if (dto.getMarkDownContent() != null) {
            page.setMarkdownContent(dto.getMarkDownContent());
        }
        if (dto.getStatus() != null) {
            page.setStatus(dto.getStatus());
        }
        if (dto.getChapterId() != null) {
            Chapter chapter = chapterRepository.findById(dto.getChapterId())
                    .orElseThrow(() -> new RuntimeException("Chapitre introuvable avec ID = "
                            + dto.getChapterId()));
            page.setChapter(chapter);
        }

        page = pageRepository.save(page);
        return PageDTO.toDto(page);
    }

    @Override
    public void delete(Long id) {
        if (!pageRepository.existsById(id)) {
            throw new RuntimeException("Page introuvable avec ID = " + id);
        }
        pageRepository.deleteById(id);
    }

    @Override
    public List<PageDTO> findByChapterId(Long chapterId) {
        List<Page> pages = pageRepository.findByChapterId(chapterId);
        return PageDTO.toDtoList(pages);
    }

}
