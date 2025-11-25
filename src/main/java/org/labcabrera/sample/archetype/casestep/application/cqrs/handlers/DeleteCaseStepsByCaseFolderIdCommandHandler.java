package org.labcabrera.sample.archetype.casestep.application.cqrs.handlers;

import org.labcabrera.sample.archetype.casestep.application.cqrs.commands.DeleteCaseStepsByCaseFolderIdCommand;
import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepRepository;
import org.labcabrera.sample.archetype.shared.application.CommandHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteCaseStepsByCaseFolderIdCommandHandler
    implements CommandHandler<DeleteCaseStepsByCaseFolderIdCommand, Void> {

    private final CaseStepRepository caseStepRepository;

    @Override
    public Void handle(DeleteCaseStepsByCaseFolderIdCommand command) {
        log.debug("Deleting case steps by case folder id {}", command.caseFolderId());
        caseStepRepository.deleteByCaseFolderId(command.caseFolderId());
        return null;
    }

}
