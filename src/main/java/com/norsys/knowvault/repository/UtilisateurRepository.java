package com.norsys.knowvault.repository;

import com.norsys.knowvault.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Utilisateur getUtilisateurByLogin(String login);
    Optional<Utilisateur> findByLogin(String login);
}
