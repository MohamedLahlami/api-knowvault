package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookService {
    BookDTO create(BookDTO dto, Authentication authentication);
    Page<BookDTO> findAll(Pageable pageable);
    BookDTO findById(Long id);
    Page<BookDTO> searchByTitle(String bookTitle, Pageable pageable);
    BookDTO update(Long id, BookDTO dto);
    void delete(Long id);
}
