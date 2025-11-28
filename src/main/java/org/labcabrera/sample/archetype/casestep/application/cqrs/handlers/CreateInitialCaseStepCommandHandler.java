package org.labcabrera.sample.archetype.casestep.application.cqrs.handlers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casestep.application.cqrs.commands.CreateInitialCaseStepCommand;
import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepEventBusPort;
import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepRepository;
import org.labcabrera.sample.archetype.casestep.application.ports.UserClientPort;
import org.labcabrera.sample.archetype.casestep.domain.CaseStep;
import org.labcabrera.sample.archetype.casestep.domain.StepStatus;
import org.labcabrera.sample.archetype.casestep.domain.StepType;
import org.labcabrera.sample.archetype.casestep.domain.events.CaseStepCreatedEvent;
import org.labcabrera.sample.archetype.shared.application.CommandHandler;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.labcabrera.sample.archetype.shared.domain.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

import com.labcabrera.sample.archetype.generated.model.CaseStepDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateInitialCaseStepCommandHandler implements CommandHandler<CreateInitialCaseStepCommand, CaseStep> {

    private final CaseStepRepository caseStepRepository;
    private final CaseFolderRepository caseFolderRepository;
    private final SecurityPort securityPort;
    private final CaseStepEventBusPort caseStepEventBusPort;
    private final UserClientPort userClientPort;

    @Override
    public CaseStep handle(CreateInitialCaseStepCommand command) {
        AuthenticatedUser user = securityPort.requireCurrentUser();
        log.info("Creating initial case step for case folder {} (user: {})", command.caseFolderId(), user.username());
        CaseFolder caseFolder = caseFolderRepository.findById(command.caseFolderId())
            .orElseThrow(() -> new BadRequestException("Case folder not found: " + command.caseFolderId()));
        StepType stepType = StepType.INITIAL_REVIEW;
        String assignedUser = userClientPort.getAssignedUser(stepType);
        CaseStep caseStep = createInitialCaseStep(caseFolder, stepType, assignedUser);
        CaseStep created = caseStepRepository.save(caseStep);
        publishCaseStepCreatedEvent(created);
        return created;
    }

    private CaseStep createInitialCaseStep(CaseFolder caseFolder, StepType stepType, String assigedUser) {
        return CaseStep.builder()
            .id(UUID.randomUUID().toString())
            .caseFolderId(caseFolder.getId())
            .stepType(stepType)
            .status(StepStatus.IN_PROGRESS)
            .assignedTo(assigedUser)
            .owner(caseFolder.getOwner())
            .createdAt(LocalDateTime.now())
            .build();
    }

    private void publishCaseStepCreatedEvent(CaseStep caseStep) {
        var event = new CaseStepCreatedEvent(caseStep.getId(), caseStep.getCaseFolderId());
        caseStepEventBusPort.publish(event);
    }

}
