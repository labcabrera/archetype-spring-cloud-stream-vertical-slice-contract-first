package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFoldersByRsqlQuery;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.shared.application.QueryHandler;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetCaseFoldersByRsqlQueryHandler implements QueryHandler<GetCaseFoldersByRsqlQuery, Page<CaseFolder>> {

    private final CaseFolderRepository caseFolderRepository;
    private final SecurityPort securityPort;

    public Page<CaseFolder> handle(GetCaseFoldersByRsqlQuery query) {
        var user = securityPort.requireCurrentUser();
        log.debug("Getting case folders by RSQL <<< {} (user: {})", query.rsql(), user.username());
        return caseFolderRepository.findByRsql(query.rsql(), query.pageable(), user);
    }
}
