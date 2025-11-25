package org.labcabrera.sample.archetype.casestep.application.cqrs.handlers;

import org.labcabrera.sample.archetype.casestep.application.cqrs.queries.GetCaseStepByIdQuery;
import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepRepository;
import org.labcabrera.sample.archetype.casestep.domain.CaseStep;
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
public class GetCaseStepByIdQueryHandler implements QueryHandler<GetCaseStepByIdQuery, CaseStep> {

    private final CaseStepRepository caseStepRepository;
    private final SecurityPort securityPort;
    private final Guard<CaseStep> caseStepGuard;

    public CaseStep handle(GetCaseStepByIdQuery query) {
        var user = securityPort.requireCurrentUser();
        log.debug("Getting case step {} (user: {})", query.caseStepId(), user.username());
        var caseStep = caseStepRepository
            .findById(query.caseStepId())
            .orElseThrow(() -> new NotFoundException("case-step.msg.not-found", query.caseStepId(), CaseStep.class));
        caseStepGuard.checkRead(caseStep, user);
        return caseStep;
    }
}
