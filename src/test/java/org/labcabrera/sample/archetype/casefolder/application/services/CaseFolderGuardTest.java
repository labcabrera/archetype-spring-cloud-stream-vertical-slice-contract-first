package org.labcabrera.sample.archetype.casefolder.application.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CaseFolderGuardTest {

    @InjectMocks
    private CaseFolderGuard caseFolderGuard;

    @Mock
    private CaseFolder caseFolder;

    private AuthenticatedUser managementUser;
    private AuthenticatedUser readUser;
    private AuthenticatedUser writeUser;
    private AuthenticatedUser unauthorizedUser;

    @BeforeEach
    void setUp() {
        managementUser = new AuthenticatedUser(
            "user-1",
            "admin",
            Set.of(CaseFolderGuard.ROLE_CASE_FOLDER_MANAGEMENT),
            Collections.emptySet());

        readUser = new AuthenticatedUser(
            "user-2",
            "reader",
            Set.of(CaseFolderGuard.ROLE_CASE_FOLDER_READ),
            Collections.emptySet());

        writeUser = new AuthenticatedUser(
            "user-3",
            "writer",
            Set.of(CaseFolderGuard.ROLE_CASE_FOLDER_WRITE),
            Collections.emptySet());

        unauthorizedUser = new AuthenticatedUser(
            "user-4",
            "guest",
            Collections.emptySet(),
            Collections.emptySet());
    }

    @Test
    void testCheckCreate_WithManagementRole_Success() {
        assertDoesNotThrow(() -> caseFolderGuard.checkCreate(managementUser));
    }

    @Test
    void testCheckCreate_WithWriteRole_Success() {
        assertDoesNotThrow(() -> caseFolderGuard.checkCreate(writeUser));
    }

    @Test
    void testCheckCreate_WithoutPermissions_ThrowsException() {
        assertThrows(SecurityException.class, () -> caseFolderGuard.checkCreate(unauthorizedUser));
    }

    @Test
    void testCheckRead_WithManagementRole_Success() {
        assertDoesNotThrow(() -> caseFolderGuard.checkRead(caseFolder, managementUser));
    }

    @Test
    void testCheckRead_WithReadRoleAndOwner_Success() {
        when(caseFolder.getOwner()).thenReturn("reader");
        assertDoesNotThrow(() -> caseFolderGuard.checkRead(caseFolder, readUser));
    }

    @Test
    void testCheckRead_WithReadRoleButNotOwner_ThrowsException() {
        when(caseFolder.getOwner()).thenReturn("otheruser");
        when(caseFolder.getId()).thenReturn("folder-123");
        assertThrows(SecurityException.class, () -> caseFolderGuard.checkRead(caseFolder, readUser));
    }

    @Test
    void testCheckRead_WithoutPermissions_ThrowsException() {
        when(caseFolder.getId()).thenReturn("folder-123");
        assertThrows(SecurityException.class, () -> caseFolderGuard.checkRead(caseFolder, unauthorizedUser));
    }

    @Test
    void testCheckWrite_WithManagementRole_Success() {
        assertDoesNotThrow(() -> caseFolderGuard.checkWrite(caseFolder, managementUser));
    }

    @Test
    void testCheckWrite_WithWriteRoleAndOwner_Success() {
        when(caseFolder.getOwner()).thenReturn("writer");
        assertDoesNotThrow(() -> caseFolderGuard.checkWrite(caseFolder, writeUser));
    }

    @Test
    void testCheckWrite_WithWriteRoleButNotOwner_ThrowsException() {
        when(caseFolder.getOwner()).thenReturn("otheruser");
        when(caseFolder.getId()).thenReturn("folder-123");
        assertThrows(SecurityException.class, () -> caseFolderGuard.checkWrite(caseFolder, writeUser));
    }

    @Test
    void testCheckWrite_WithoutPermissions_ThrowsException() {
        when(caseFolder.getId()).thenReturn("folder-123");
        assertThrows(SecurityException.class, () -> caseFolderGuard.checkWrite(caseFolder, unauthorizedUser));
    }
}
