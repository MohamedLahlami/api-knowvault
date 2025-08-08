package com.norsys.knowvault.repository;

import com.norsys.knowvault.model.Favorite;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(Utilisateur user);
    Optional<Favorite> findByUserAndPage(Utilisateur user, Page page);
}
