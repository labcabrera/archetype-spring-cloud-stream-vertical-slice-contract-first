package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime;

@Entity
@Table(name = "case_folder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseFolderEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CaseFolderStatus status;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "first_surname", nullable = false, length = 100)
    private String firstSurname;

    @Column(name = "last_surname", nullable = true, length = 100)
    private String lastSurname;

    @Embedded
    private IdCardEntity idCard;

    @Column(name = "owner", nullable = false, length = 100)
    private String owner;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

}