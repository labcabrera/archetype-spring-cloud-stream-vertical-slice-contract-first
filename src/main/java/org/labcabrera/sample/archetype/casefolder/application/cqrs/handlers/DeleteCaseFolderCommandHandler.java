package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.DeleteCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderEventBusPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderMetricPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderDeletedEvent;
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
public class DeleteCaseFolderCommandHandler implements CommandHandler<DeleteCaseFolderCommand, Void> {

    private final CaseFolderRepository caseFolderRepository;
    private final CaseFolderEventBusPort caseFolderEventBusPort;
    private final Guard<CaseFolder> caseFolderGuard;
    private final SecurityPort securityPort;
    private final CaseFolderMetricPort caseFolderMetricPort;

    @Override
    public Void handle(DeleteCaseFolderCommand command) {
        var user = securityPort.requireCurrentUser();
        log.debug("Deleting case folder {} (user: {})", command.caseFolderId(), user.username());
        var caseFolder = caseFolderRepository.findById(command.caseFolderId())
            .orElseThrow(() -> new NotFoundException("case-folder.msg.not-found", command.caseFolderId(), CaseFolder.class));
        caseFolderGuard.checkWrite(caseFolder, user);
        caseFolderRepository.deleteById(command.caseFolderId());
        sendNotification(caseFolder);
        caseFolderMetricPort.incrementCaseFolderDeletedCounter();
        return null;
    }

    private void sendNotification(CaseFolder caseFolder) {
        var event = new CaseFolderDeletedEvent(
            caseFolder.getId(),
            caseFolder.getUserInfo());
        caseFolderEventBusPort.publish(event);
    }
}
