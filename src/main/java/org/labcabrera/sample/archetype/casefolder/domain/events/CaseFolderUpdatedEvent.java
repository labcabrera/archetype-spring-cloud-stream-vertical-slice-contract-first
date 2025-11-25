package org.labcabrera.sample.archetype.casefolder.domain.events;

public record CaseFolderUpdatedEvent(
    String id,
    String name,
    String firstSurname,
    String secondSurname) {
}
