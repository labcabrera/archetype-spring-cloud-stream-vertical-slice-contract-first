package org.labcabrera.sample.archetype.casefolder.application.cqrs.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFoldersByRsqlQuery;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.IdCard;
import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class GetCaseFoldersByRsqlQueryHandlerTest {

    @Mock
    private CaseFolderRepository caseFolderRepository;

    @Mock
    private SecurityPort securityPort;

    @InjectMocks
    private GetCaseFoldersByRsqlQueryHandler handler;

    private AuthenticatedUser authenticatedUser;
    private CaseFolder caseFolder1;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        authenticatedUser = new AuthenticatedUser(
            "user-1",
            "testuser",
            Set.of("case-folder-read"),
            Collections.emptySet());
        var userInfo = org.labcabrera.sample.archetype.casefolder.domain.UserInfo.builder()
            .id(null)
            .name("JOHN")
            .firstSurname("DOE")
            .lastSurname(java.util.Optional.of("SMITH"))
            .idCard(new IdCard("12345678A", IdCardType.NIF))
            .build();

        caseFolder1 = CaseFolder.create(userInfo, "testuser");
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testHandle_WithValidRsql_ReturnsFilteredResults() {
        String rsql = "name==JOHN";
        GetCaseFoldersByRsqlQuery query = new GetCaseFoldersByRsqlQuery(rsql, pageable);
        Page<CaseFolder> expectedPage = new PageImpl<>(List.of(caseFolder1), pageable, 1);

        when(securityPort.requireCurrentUser()).thenReturn(authenticatedUser);
        when(caseFolderRepository.findByRsql(rsql, pageable, authenticatedUser))
            .thenReturn(expectedPage);

        Page<CaseFolder> result = handler.handle(query);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("JOHN", result.getContent().get(0).getUserInfo().getName());
        verify(securityPort).requireCurrentUser();
        verify(caseFolderRepository).findByRsql(rsql, pageable, authenticatedUser);
    }

}
