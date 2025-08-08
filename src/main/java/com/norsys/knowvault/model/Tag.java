package com.norsys.knowvault.model;

import com.norsys.knowvault.enumerator.TagType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @Enumerated(EnumType.STRING)
    private TagType type;


    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Book> books;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<Shelf> shelves;
}
