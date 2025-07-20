package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.BookDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookService {
    BookDTO create(BookDTO dto, Authentication authentication);
    List<BookDTO> findAll();
    BookDTO findById(Long id);
    BookDTO update(Long id, BookDTO dto);
    void delete(Long id);
}
