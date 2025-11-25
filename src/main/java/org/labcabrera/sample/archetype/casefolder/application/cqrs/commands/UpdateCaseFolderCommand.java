package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

import org.labcabrera.sample.archetype.casefolder.domain.IdCard;

public record UpdateCaseFolderCommand(
    String caseFolderId,
    String name,
    String firstSurname,
    String lastSurname,
    IdCard idCard) {
}
