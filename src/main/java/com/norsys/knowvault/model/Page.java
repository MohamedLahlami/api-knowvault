package com.norsys.knowvault.model;

import com.norsys.knowvault.enumerator.PageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int pageNumber;
    private String content;
    private String markdownContent;

    @Enumerated(EnumType.STRING)
    private PageStatus status;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<History> histories;

}
