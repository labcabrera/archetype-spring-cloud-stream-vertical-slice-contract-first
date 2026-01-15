package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderEventBusPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderMetricPort;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;
import org.labcabrera.sample.archetype.casefolder.domain.IdCard;
import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.micrometer.core.instrument.Counter;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreateCaseFolderCommandHandlerTest {

    @Mock
    private CaseFolderRepository caseFolderRepository;

    @Mock
    private CaseFolderEventBusPort caseFolderEventBusPort;

    @Mock
    private SecurityPort securityPort;

    @Mock
    private Guard<CaseFolder> caseFolderGuard;

    @Mock
    private Validator validator;

    @Mock
    private CaseFolderMetricPort caseFolderMetricPort;

    @Mock
    private Counter counter;

    @InjectMocks
    private CreateCaseFolderCommandHandler handler;

    private CreateCaseFolderCommand command;
    private AuthenticatedUser authenticatedUser;
    private CaseFolder caseFolder;
    private UserInfo userInfo;

    @BeforeEach
    void setUp() {
        command = new CreateCaseFolderCommand(
            "John",
            "Doe",
            "Smith",
            IdCardType.NIF,
            "12345678A");

        authenticatedUser = new AuthenticatedUser(
            "user-id-123",
            "testuser",
            Set.of("case-folder-management"),
            Collections.emptySet());

        userInfo = UserInfo.builder()
            .id(null)
            .name(command.name())
            .firstSurname(command.firstSurname())
            .lastSurname(java.util.Optional.ofNullable(command.lastSurname()))
            .idCard(new IdCard(command.idCardNumber(), command.idCardType()))
            .build();

        caseFolder = CaseFolder.create(userInfo, authenticatedUser.username());
    }

    @Test
    void testHandle_success() {
        when(securityPort.requireCurrentUser()).thenReturn(authenticatedUser);
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(caseFolderRepository.save(any(CaseFolder.class))).thenReturn(caseFolder);

        CaseFolder result = handler.handle(command);

        assertNotNull(result);
        assertEquals(command.name().toUpperCase(), result.getUserInfo().getName());
        assertEquals(command.firstSurname().toUpperCase(), result.getUserInfo().getFirstSurname());
        assertEquals(command.lastSurname().toUpperCase(), result.getUserInfo().getLastSurname().orElse(null));
        assertEquals(command.idCardNumber().toUpperCase(), result.getUserInfo().getIdCard().idCardNumber());
        assertEquals(command.idCardType(), result.getUserInfo().getIdCard().idCardType());

        verify(securityPort).requireCurrentUser();
        verify(caseFolderGuard).checkCreate(authenticatedUser);
        verify(caseFolderRepository).save(any(CaseFolder.class));
        verify(caseFolderEventBusPort).publish(any(CaseFolderCreatedEvent.class));
    }

    @Test
    void testHandle_notAllowed() {
        when(securityPort.requireCurrentUser()).thenReturn(authenticatedUser);
        doThrow(new SecurityException("Not allowed"))
            .when(caseFolderGuard).checkCreate(any(AuthenticatedUser.class));
        assertThrows(SecurityException.class, () -> {
            handler.handle(command);
        });
        verify(securityPort).requireCurrentUser();
    }

}
