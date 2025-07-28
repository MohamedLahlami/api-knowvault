package com.norsys.knowvault.repository;

import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    List<Page> findByChapterId(Long chapterId);


}
