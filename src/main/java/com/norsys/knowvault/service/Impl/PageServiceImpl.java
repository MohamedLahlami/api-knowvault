package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.PageDTO;
import com.norsys.knowvault.model.Chapter;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.repository.ChapterRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.service.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public PageDTO create(PageDTO dto) {
        log.debug("Creating page with pageNumber: {} for chapter ID: {}", dto.getPageNumber(), dto.getChapterId());

        Chapter chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> {
                    log.error("Chapter not found with ID: {}", dto.getChapterId());
                    return new RuntimeException("Chapitre introuvable avec ID = " + dto.getChapterId());
                });

        log.debug("Found chapter: {} for page creation", chapter.getId());

        Page page = new Page();
        page.setPageNumber(dto.getPageNumber());
        page.setContent(dto.getContent());
        page.setMarkdownContent(dto.getMarkDownContent());
        page.setStatus(dto.getStatus());
        page.setChapter(chapter);

        log.debug("Saving page to database");
        page = pageRepository.save(page);
        log.info("Successfully created page with ID: {} in chapter: {}", page.getId(), chapter.getId());

        return PageDTO.toDto(page);
    }

    @Override
    public List<PageDTO> findAll() {
        log.debug("Fetching all pages from database");
        List<Page> pages = pageRepository.findAll();
        log.info("Found {} pages in database", pages.size());
        return PageDTO.toDtoList(pages);
    }

    @Override
    public PageDTO findById(Long id) {
        log.debug("Searching for page with ID: {}", id);
        Page page = pageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Page not found with ID: {}", id);
                    return new RuntimeException("Page introuvable avec ID = " + id);
                });
        log.debug("Successfully found page with ID: {}", id);
        return PageDTO.toDto(page);
    }

    @Override
    public PageDTO update(Long id, PageDTO dto) {
        log.debug("Updating page with ID: {}", id);

        Page page = pageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Page not found for update with ID: {}", id);
                    return new RuntimeException("Page introuvable avec ID = " + id);
                });

        log.debug("Found existing page with ID: {}, applying updates", id);

        if (dto.getPageNumber() != null) {
            log.debug("Updating page number from {} to {}", page.getPageNumber(), dto.getPageNumber());
            page.setPageNumber(dto.getPageNumber());
        }
        if (dto.getContent() != null) {
            log.debug("Updating page content (length: {})", dto.getContent().length());
            page.setContent(dto.getContent());
        }
        if (dto.getMarkDownContent() != null) {
            log.debug("Updating page markdown content (length: {})", dto.getMarkDownContent().length());
            page.setMarkdownContent(dto.getMarkDownContent());
        }
        if (dto.getStatus() != null) {
            log.debug("Updating page status to: {}", dto.getStatus());
            page.setStatus(dto.getStatus());
        }
        if (dto.getChapterId() != null) {
            log.debug("Updating page chapter to ID: {}", dto.getChapterId());
            Chapter chapter = chapterRepository.findById(dto.getChapterId())
                    .orElseThrow(() -> {
                        log.error("Chapter not found for page update with ID: {}", dto.getChapterId());
                        return new RuntimeException("Chapitre introuvable avec ID = " + dto.getChapterId());
                    });
            page.setChapter(chapter);
        }

        page = pageRepository.save(page);
        log.info("Successfully updated page with ID: {}", id);
        return PageDTO.toDto(page);
    }

    @Override
    public void delete(Long id) {
        log.debug("Attempting to delete page with ID: {}", id);

        if (!pageRepository.existsById(id)) {
            log.error("Cannot delete page - not found with ID: {}", id);
            throw new RuntimeException("Page introuvable avec ID = " + id);
        }

        pageRepository.deleteById(id);
        log.info("Successfully deleted page with ID: {}", id);
    }

    @Override
    public List<PageDTO> findByChapterId(Long chapterId) {
        log.debug("Fetching pages for chapter ID: {}", chapterId);
        List<Page> pages = pageRepository.findByChapterId(chapterId);
        log.info("Found {} pages for chapter ID: {}", pages.size(), chapterId);
        return PageDTO.toDtoList(pages);
    }
}
