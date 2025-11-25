package org.labcabrera.sample.archetype.casefolder.application.cqrs.commands;

import org.labcabrera.sample.archetype.casefolder.domain.CaseFolderStatus;

public record UpdateCaseFolderStatusCommand(
    String caseFolderId,
    CaseFolderStatus status) {
}
