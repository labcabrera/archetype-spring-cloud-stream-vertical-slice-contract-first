package org.labcabrera.sample.archetype.casefolder.application.cqrs.queries;

import org.springframework.data.domain.Pageable;

public record GetCaseFoldersByRsqlQuery(
    String rsql,
    Pageable pageable) {
}
