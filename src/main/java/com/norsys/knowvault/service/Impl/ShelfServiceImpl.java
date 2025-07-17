package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.ShelfDTO;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.repository.ShelfRepository;
import com.norsys.knowvault.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository shelfRepository;

    @Override
    public ShelfDTO create(ShelfDTO dto) {
        Shelf etagere = Shelf.builder()
                .label(dto.getLabel())
                .description(dto.getDescription())
                .tag(dto.getTag())
                .build();

        return ShelfDTO.toDto(shelfRepository.save(etagere));
    }

    @Override
    public List<ShelfDTO> findAll() {
        return ShelfDTO.toDtoList(shelfRepository.findAll());
    }

    @Override
    public ShelfDTO findById(Long id) {
        return ShelfDTO.toDto(shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etagere introuvable")));
    }

    @Override
    public ShelfDTO update(Long id, ShelfDTO dto) {
        Shelf etagere = shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etagere introuvable"));

        if (dto.getLabel() != null) etagere.setLabel(dto.getLabel());
        if (dto.getDescription() != null) etagere.setDescription(dto.getDescription());
        if (dto.getTag() != null) etagere.setTag(dto.getTag());

        return ShelfDTO.toDto(shelfRepository.save(etagere));
    }

    @Override
    public void delete(Long id) {
        if (!shelfRepository.existsById(id)) {
            throw new RuntimeException("Etagere introuvable");
        }
        shelfRepository.deleteById(id);
    }

}
