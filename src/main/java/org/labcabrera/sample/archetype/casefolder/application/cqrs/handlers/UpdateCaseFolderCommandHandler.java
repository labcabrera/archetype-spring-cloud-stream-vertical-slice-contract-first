package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import java.util.Optional;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.UpdateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderEventBusPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderMetricPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderUpdatedEvent;
import org.labcabrera.sample.archetype.shared.application.CommandHandler;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateCaseFolderCommandHandler implements CommandHandler<UpdateCaseFolderCommand, CaseFolder> {

    private final CaseFolderRepository caseFolderRepository;
    private final CaseFolderEventBusPort caseFolderEventBusPort;
    private final Guard<CaseFolder> caseFolderGuard;
    private final SecurityPort securityPort;
    private final CaseFolderMetricPort caseFolderMetricPort;

    public CaseFolder handle(UpdateCaseFolderCommand command) {
        var caseFolderId = command.caseFolderId();
        var user = securityPort.requireCurrentUser();
        log.info("Update case folder << {} (user: {})", caseFolderId, user.username());
        var existing = caseFolderRepository.findById(caseFolderId)
            .orElseThrow(() -> new NotFoundException("case-folder.msg.not-found", caseFolderId, CaseFolder.class));
        caseFolderGuard.checkWrite(existing, user);
        var update = CaseFolder.builder()
            .id(caseFolderId)
            .userInfo(UserInfo.builder()
                .name(command.name())
                .firstSurname(command.firstSurname())
                .lastSurname(Optional.ofNullable(command.lastSurname()))
                .idCard(command.idCard())
                .build()
                .normalize())
            .build();
        var updated = caseFolderRepository.update(caseFolderId, update);
        sendNotification(updated);
        caseFolderMetricPort.incrementCaseFolderUpdatedCounter();
        return updated;
    }

    private void sendNotification(CaseFolder caseFolder) {
        var event = new CaseFolderUpdatedEvent(
            caseFolder.getId(),
            caseFolder.getUserInfo());
        caseFolderEventBusPort.publish(event);
    }
}
