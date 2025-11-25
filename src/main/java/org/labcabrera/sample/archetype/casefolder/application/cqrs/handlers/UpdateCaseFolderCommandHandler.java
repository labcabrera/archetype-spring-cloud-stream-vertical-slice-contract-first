package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.UpdateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderEventBusPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderMetricPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderUpdatedEvent;
import org.labcabrera.sample.archetype.shared.application.CommandHandler;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotFoundException;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotModifiedException;
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
        var user = securityPort.requireCurrentUser();
        log.info("Update case folder << {} (user: {})", command.caseFolderId(), user.username());
        var existing = caseFolderRepository.findById(command.caseFolderId())
            .orElseThrow(() -> new NotFoundException("case-folder.msg.not-found", command.caseFolderId(), CaseFolder.class));
        caseFolderGuard.checkWrite(existing, user);
        merge(existing, command);
        existing.normalize();
        var caseFolder = caseFolderRepository.update(existing);
        sendNotification(caseFolder);
        caseFolderMetricPort.incrementCaseFolderUpdatedCounter();
        return caseFolder;
    }

    private void merge(CaseFolder existing, UpdateCaseFolderCommand command) {
        boolean modified = false;
        if (command.name() != null && !command.name().toUpperCase().equals(existing.getName())) {
            existing.setName(command.name());
            modified = true;
        }
        if (command.firstSurname() != null && !command.firstSurname().toUpperCase().equals(existing.getFirstSurname())) {
            existing.setFirstSurname(command.firstSurname());
            modified = true;
        }
        if (command.lastSurname() != null && !command.lastSurname().toUpperCase().equals(existing.getLastSurname())) {
            existing.setLastSurname(command.lastSurname());
            modified = true;
        }
        if (!modified) {
            throw new NotModifiedException("case-folder.msg.err.not-modified");
        }
    }

    private void sendNotification(CaseFolder caseFolder) {
        var event = new CaseFolderUpdatedEvent(
            caseFolder.getId(),
            caseFolder.getName(),
            caseFolder.getFirstSurname(),
            caseFolder.getLastSurname());
        caseFolderEventBusPort.publish(event);
    }
}
