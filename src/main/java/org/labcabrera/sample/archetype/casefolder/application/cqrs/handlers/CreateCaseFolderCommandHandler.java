package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import java.util.Optional;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderEventBusPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderMetricPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.IdCard;
import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.shared.application.CommandHandler;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateCaseFolderCommandHandler implements CommandHandler<CreateCaseFolderCommand, CaseFolder> {

    private final CaseFolderRepository caseFolderRepository;
    private final CaseFolderEventBusPort caseFolderEventBusPort;
    private final SecurityPort securityPort;
    private final Guard<CaseFolder> caseFolderGuard;
    private final Validator validator;
    private final CaseFolderMetricPort caseFolderMetricPort;

    public CaseFolder handle(@Validated CreateCaseFolderCommand command) {
        var user = securityPort.requireCurrentUser();
        log.info("Create case folder << {} (user: {})", command.idCardNumber(), user.username());
        caseFolderGuard.checkCreate(user);
        var caseFolder = buildCaseFolderFromCommand(command, user.username());
        validateCaseFolder(caseFolder);
        var created = caseFolderRepository.save(caseFolder);
        caseFolderMetricPort.incrementCaseFolderCreatedCounter();
        sendNotification(created);
        return created;
    }

    private void validateCaseFolder(CaseFolder caseFolder) {
        var violations = validator.validate(caseFolder);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("case-folder.msg.err.validation-error", violations);
        }
    }

    private CaseFolder buildCaseFolderFromCommand(CreateCaseFolderCommand command, String username) {
        return CaseFolder.create(
            UserInfo.builder()
                .name(command.name())
                .firstSurname(command.firstSurname())
                .lastSurname(Optional.ofNullable(command.lastSurname()))
                .idCard(new IdCard(command.idCardNumber(), command.idCardType()))
                .build(),
            username);
    }

    private void sendNotification(CaseFolder caseFolder) {
        var event = new CaseFolderCreatedEvent(
            caseFolder.getId(),
            caseFolder.getUserInfo(),
            caseFolder.getCreatedAt());
        caseFolderEventBusPort.publish(event);
    }

}