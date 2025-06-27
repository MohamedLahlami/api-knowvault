package com.norsys.knowvault.repository;

import com.norsys.knowvault.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur getUtilisateurByLogin(String login);
}
