package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.UpdateCaseFolderStatusCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.shared.application.CommandHandler;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateCaseFolderStatusCommandHandler implements CommandHandler<UpdateCaseFolderStatusCommand, CaseFolder> {

    private final CaseFolderRepository caseFolderRepository;
    private final Guard<CaseFolder> caseFolderGuard;
    private final SecurityPort securityPort;

    @Override
    public CaseFolder handle(UpdateCaseFolderStatusCommand command) {
        var current = caseFolderRepository.findById(command.caseFolderId())
            .orElseThrow(() -> new NotFoundException("case-folder.msg.not-found", command.caseFolderId(), CaseFolder.class));
        var user = securityPort.requireCurrentUser();
        caseFolderGuard.checkWrite(current, user);
        return caseFolderRepository.updateStatus(current.getId(), command.status());
    }

}
