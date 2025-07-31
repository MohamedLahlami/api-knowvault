package com.norsys.knowvault.model;

import com.norsys.knowvault.enumerator.TagType;
import jakarta.persistence.*;
import lombok.*;


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
    private TagType type; // BOOK or SHELF

    private Long resourceId; // Book or shelf ID
}
