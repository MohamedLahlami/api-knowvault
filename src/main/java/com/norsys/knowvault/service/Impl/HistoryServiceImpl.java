package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.HistoryDTO;
import com.norsys.knowvault.model.History;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.repository.HistoryRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final PageRepository pageRepository;

    @Override
    public HistoryDTO create(HistoryDTO dto) {
        Page page = pageRepository.findById(dto.getPageId())
                .orElseThrow(() -> new RuntimeException("Page introuvable"));

        History history = History.builder()
                .modificationDate(dto.getModificationDate())
                .modificationType(dto.getModificationType())
                .page(page)
                .build();

        return HistoryDTO.toDto(historyRepository.save(history));
    }

    @Override
    public List<HistoryDTO> findAll() {
        return HistoryDTO.toDtoList(historyRepository.findAll());
    }

    @Override
    public HistoryDTO findById(Long id) {
        return HistoryDTO.toDto(historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historiy introuvable")));
    }

    @Override
    public HistoryDTO update(Long id, HistoryDTO dto) {
        History history = historyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historiy introuvable"));

        if (dto.getModificationDate() != null) {
            history.setModificationDate(dto.getModificationDate());
        }
        if (dto.getModificationType() != null) {
            history.setModificationType(dto.getModificationType());
        }

        if (dto.getPageId() != null) {
            Page p = pageRepository.findById(dto.getPageId())
                    .orElseThrow(() -> new RuntimeException("Page introuvable"));
            history.setPage(p);
        }

        return HistoryDTO.toDto(historyRepository.save(history));
    }

    @Override
    public void delete(Long id) {
        if (!historyRepository.existsById(id)) {
            throw new RuntimeException("Historiy introuvable");
        }
        historyRepository.deleteById(id);
    }

}
