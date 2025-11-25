package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFolderByIdQuery;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.QueryHandler;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotFoundException;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetCaseFolderByIdQueryHandler implements QueryHandler<GetCaseFolderByIdQuery, CaseFolder> {

    private final CaseFolderRepository caseFolderRepository;
    private final SecurityPort securityPort;
    private final Guard<CaseFolder> caseFolderGuard;

    public CaseFolder handle(GetCaseFolderByIdQuery query) {
        var user = securityPort.requireCurrentUser();
        log.debug("Getting case folder {} (user: {})", query.caseFolderId(), user.username());
        var caseFolder = caseFolderRepository
            .findById(query.caseFolderId())
            .orElseThrow(() -> new NotFoundException("case-folder.msg.not-found", query.caseFolderId(), CaseFolder.class));
        caseFolderGuard.checkRead(caseFolder, user);
        return caseFolder;
    }

}