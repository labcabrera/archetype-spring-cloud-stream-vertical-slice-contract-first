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
    private String name;

    @NotNull
    private String firstSurname;

    private String lastSurname;

    @NotNull
    private IdCard idCard;

    @NotNull
    private String owner;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static CaseFolder create(String name, String firstSurname, String lastSurname, IdCard idCard, String owner) {
        return CaseFolder.builder()
            .id(UUID.randomUUID().toString())
            .status(CaseFolderStatus.PARTIALLY_CREATED)
            .name(name)
            .firstSurname(firstSurname)
            .lastSurname(lastSurname)
            .idCard(idCard)
            .owner(owner)
            .createdAt(LocalDateTime.now())
            .build()
            .normalize();
    }

    public CaseFolder normalize() {
        name = name.toUpperCase();
        firstSurname = firstSurname.toUpperCase();
        if (lastSurname != null) {
            lastSurname = lastSurname.toUpperCase();
        }
        return this;
    }

}