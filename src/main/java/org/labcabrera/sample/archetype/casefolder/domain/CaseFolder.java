package org.labcabrera.sample.archetype.casefolder.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Domain model representing a case folder.
 * 
 * A CaseFolder represents a case file associated with a user. A case file will be
 * associated with different procedures (CaseSteps).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class CaseFolder {

    @NotNull
    private String id;

    @NotNull
    private CaseFolderStatus status;

    @NotNull
    private UserInfo userInfo;

    @NotNull
    private String owner;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static CaseFolder create(UserInfo userInfo, String owner) {
        UserInfo normalizedUserInfo = UserInfo.builder()
            .id(userInfo.getId() != null ? userInfo.getId() : UUID.randomUUID().toString())
            .name(userInfo.getName())
            .firstSurname(userInfo.getFirstSurname())
            .lastSurname(userInfo.getLastSurname())
            .idCard(userInfo.getIdCard())
            .build()
            .normalize();
        return CaseFolder.builder()
            .id(UUID.randomUUID().toString())
            .status(CaseFolderStatus.PARTIALLY_CREATED)
            .userInfo(normalizedUserInfo)
            .owner(owner)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public boolean merge(CaseFolder updated) {
        boolean modified = false;
        if (updated.getUserInfo() != null) {
            modified |= this.userInfo.merge(updated.getUserInfo());
        }
        if (this.status != null && !this.status.equals(updated.getStatus())) {
            this.status = updated.getStatus();
            modified = true;
        }
        return modified;
    }

}