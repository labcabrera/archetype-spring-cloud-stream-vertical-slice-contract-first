package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private UserInfoEntity userInfo;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CaseFolderStatus status;

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

    public boolean merge(CaseFolder updated) {
        boolean modified = false;
        if (updated.getUserInfo() != null) {
            modified |= this.userInfo.merge(updated.getUserInfo());
        }
        if (updated.getStatus() != null && !updated.getStatus().equals(this.status)) {
            this.status = updated.getStatus();
            modified = true;
        }
        return modified;
    }

}