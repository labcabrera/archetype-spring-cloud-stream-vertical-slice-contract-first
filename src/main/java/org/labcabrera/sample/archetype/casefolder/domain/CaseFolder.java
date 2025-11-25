package org.labcabrera.sample.archetype.casefolder.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        return CaseFolder.builder()
            .id(UUID.randomUUID().toString())
            .status(CaseFolderStatus.PARTIALLY_CREATED)
            .userInfo(userInfo.normalize())
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