package com.norsys.knowvault.repository;

import com.norsys.knowvault.enumerator.TagType;
import com.norsys.knowvault.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByType(TagType type);

}
