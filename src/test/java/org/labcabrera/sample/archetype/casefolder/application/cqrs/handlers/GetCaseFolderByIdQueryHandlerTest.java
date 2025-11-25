package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFolderByIdQuery;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.IdCard;
import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetCaseFolderByIdQueryHandlerTest {

    @Mock
    private CaseFolderRepository caseFolderRepository;

    @Mock
    private SecurityPort securityPort;

    @Mock
    private Guard<CaseFolder> caseFolderGuard;

    @InjectMocks
    private GetCaseFolderByIdQueryHandler handler;

    private GetCaseFolderByIdQuery query;
    private AuthenticatedUser authenticatedUser;
    private CaseFolder caseFolder;

    @BeforeEach
    void setUp() {
        authenticatedUser = new AuthenticatedUser(
            "user-id-123",
            "testuser",
            Set.of("case-folder-read"),
            Collections.emptySet());

        caseFolder = CaseFolder.create(
            "JOHN",
            "DOE",
            "SMITH",
            new IdCard("12345678A", IdCardType.NIF),
            "testuser");

        query = new GetCaseFolderByIdQuery(caseFolder.getId());
    }

    @Test
    void testHandle_Success() {
        when(securityPort.requireCurrentUser()).thenReturn(authenticatedUser);
        when(caseFolderRepository.findById(query.caseFolderId())).thenReturn(Optional.of(caseFolder));

        CaseFolder result = handler.handle(query);

        assertNotNull(result);
        assertEquals(caseFolder.getId(), result.getId());
        assertEquals("JOHN", result.getName());
        assertEquals("DOE", result.getFirstSurname());
        verify(securityPort).requireCurrentUser();
        verify(caseFolderRepository).findById(query.caseFolderId());
        verify(caseFolderGuard).checkRead(caseFolder, authenticatedUser);
    }

    @Test
    void testHandle_CaseFolderNotFound_ThrowsNotFoundException() {
        when(securityPort.requireCurrentUser()).thenReturn(authenticatedUser);
        when(caseFolderRepository.findById(query.caseFolderId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> handler.handle(query));

        verify(caseFolderRepository).findById(query.caseFolderId());
        verify(caseFolderGuard, never()).checkRead(any(), any());
    }
}
