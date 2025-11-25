package org.labcabrera.sample.archetype.casefolder.domain.events;

import java.time.LocalDateTime;

import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;

public record CaseFolderCreatedEvent(
    String id,
    String name,
    String firstSurname,
    String secondSurname,
    IdCardType idCardType,
    String idCardNumber,
    LocalDateTime createdAt) {
}
