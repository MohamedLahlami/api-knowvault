package com.norsys.knowvault.repository;

import com.norsys.knowvault.dto.TagDTO;
import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    List<Shelf> findTop3ByOrderByViewsDesc(Pageable pageable);

    @Query("SELECT new com.norsys.knowvault.dto.TagDTO(t.id, t.label, t.type, COUNT(s)) " +
            "FROM Shelf s JOIN s.tag t GROUP BY t.id, t.label, t.type")
    List<TagDTO> countShelvesByTag();
}
