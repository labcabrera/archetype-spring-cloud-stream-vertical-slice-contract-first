package org.labcabrera.sample.archetype.casefolder.domain.events;

import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;

public record CaseFolderDeletedEvent(
    String caseFolderId,
    IdCardType idCardType,
    String idCardNumber) {
}
