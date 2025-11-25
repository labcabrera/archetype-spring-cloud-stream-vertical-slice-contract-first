package org.labcabrera.sample.archetype.casefolder.domain.events;

import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;

public record CaseFolderDeletedEvent(
    String caseFolderId,
    UserInfo userInfo) {
}
