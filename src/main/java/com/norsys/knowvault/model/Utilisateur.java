package com.norsys.knowvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    @Id
    private UUID id;
    private String login;
    private String lastName;
    private String firstName;
    private String idUserType;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
