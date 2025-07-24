package com.norsys.knowvault.repository;

import com.norsys.knowvault.model.Shelf;
import com.norsys.knowvault.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {
}
