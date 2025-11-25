package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;

import jakarta.validation.constraints.NotNull;

public record CreateCaseFolderCommand(

    @NotNull String name,

    @NotNull String firstSurname,

    String lastSurname,

    @NotNull IdCardType idCardType,

    @NotNull String idCardNumber

) {
}
