package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

public record UpdateCaseFolderCommand(
    String caseFolderId,
    String name,
    String firstSurname,
    String lastSurname) {
}
