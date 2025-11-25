package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.DeleteCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderEventBusPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderMetricPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.IdCard;
import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderDeletedEvent;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteCaseFolderCommandHandlerTest {

    @Mock
    private CaseFolderRepository caseFolderRepository;

    @Mock
    private CaseFolderEventBusPort caseFolderEventBusPort;

    @Mock
    private Guard<CaseFolder> caseFolderGuard;

    @Mock
    private SecurityPort securityPort;

    @Mock
    private CaseFolderMetricPort caseFolderMetricPort;

    @InjectMocks
    private DeleteCaseFolderCommandHandler handler;

    private DeleteCaseFolderCommand command;
    private AuthenticatedUser authenticatedUser;
    private CaseFolder caseFolder;

    @BeforeEach
    void setUp() {
        authenticatedUser = new AuthenticatedUser(
            "user-id-123",
            "testuser",
            Set.of("case-folder-write"),
            Collections.emptySet());
        caseFolder = CaseFolder.create(
            "JOHN",
            "DOW",
            "SMITH",
            new IdCard("12345678A", IdCardType.NIF),
            "testuser");
        command = new DeleteCaseFolderCommand(caseFolder.getId());
    }

    @Test
    void testHandle_Success() {
        when(securityPort.requireCurrentUser()).thenReturn(authenticatedUser);
        when(caseFolderRepository.findById(command.caseFolderId())).thenReturn(Optional.of(caseFolder));

        handler.handle(command);

        verify(securityPort).requireCurrentUser();
        verify(caseFolderRepository).findById(command.caseFolderId());
        verify(caseFolderGuard).checkWrite(caseFolder, authenticatedUser);
        verify(caseFolderRepository).deleteById(command.caseFolderId());
        verify(caseFolderEventBusPort).publish(any(CaseFolderDeletedEvent.class));
        verify(caseFolderMetricPort).incrementCaseFolderDeletedCounter();
    }

    @Test
    void testHandle_CaseFolderNotFound_ThrowsNotFoundException() {
        when(securityPort.requireCurrentUser()).thenReturn(authenticatedUser);
        when(caseFolderRepository.findById(command.caseFolderId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> handler.handle(command));

        verify(caseFolderRepository).findById(command.caseFolderId());
        verify(caseFolderGuard, never()).checkWrite(any(), any());
        verify(caseFolderRepository, never()).deleteById(any());
        verify(caseFolderEventBusPort, never()).publish(any(CaseFolderDeletedEvent.class));
    }
}
