package com.norsys.knowvault.model;

import com.norsys.knowvault.enumerator.ModificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime modificationDate;
    private ModificationType modificationType;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page;
}
