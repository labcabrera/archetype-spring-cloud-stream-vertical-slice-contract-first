package org.labcabrera.sample.archetype.casefolder.domain.events;

import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;

/**
 * Event emitted when a case folder is deleted.
 */
public record CaseFolderDeletedEvent(
    String caseFolderId,
    UserInfo userInfo) {
}
