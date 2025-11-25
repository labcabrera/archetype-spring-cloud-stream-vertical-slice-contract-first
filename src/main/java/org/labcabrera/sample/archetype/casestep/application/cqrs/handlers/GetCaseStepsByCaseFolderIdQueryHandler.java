package org.labcabrera.sample.archetype.casestep.application.cqrs.handlers;

import java.util.List;

import org.labcabrera.sample.archetype.casestep.application.cqrs.queries.GetCaseStepsByCaseFolderIdQuery;
import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepRepository;
import org.labcabrera.sample.archetype.casestep.domain.CaseStep;
import org.labcabrera.sample.archetype.shared.application.QueryHandler;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetCaseStepsByCaseFolderIdQueryHandler implements QueryHandler<GetCaseStepsByCaseFolderIdQuery, List<CaseStep>> {

    private final CaseStepRepository caseStepRepository;
    private final SecurityPort securityPort;

    @Override
    public List<CaseStep> handle(GetCaseStepsByCaseFolderIdQuery query) {
        var user = securityPort.requireCurrentUser();
        log.debug("Getting case steps for case folder {} (user={})", query.caseFolderId(), user.username());
        return caseStepRepository.findByCaseFolderId(query.caseFolderId());
    }

}
