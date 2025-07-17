package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO create(BookDTO dto);
    List<BookDTO> findAll();
    BookDTO findById(Long id);
    BookDTO update(Long id, BookDTO dto);
    void delete(Long id);
}
