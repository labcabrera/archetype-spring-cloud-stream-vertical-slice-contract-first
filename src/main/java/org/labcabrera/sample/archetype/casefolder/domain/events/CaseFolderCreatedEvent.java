package org.labcabrera.sample.archetype.casefolder.domain.events;

import java.time.LocalDateTime;

import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;

/**
 * Event emitted when a case folder is created.
 */
public record CaseFolderCreatedEvent(
    String id,
    UserInfo userInfo,
    LocalDateTime createdAt) {
}
