package org.labcabrera.sample.archetype.casefolder.application.services;

import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class CaseFolderGuard implements Guard<CaseFolder> {

    public static final String ROLE_CASE_FOLDER_MANAGEMENT = "case-folder-management";
    public static final String ROLE_CASE_FOLDER_READ = "case-folder-read";
    public static final String ROLE_CASE_FOLDER_WRITE = "case-folder-write";

    @Override
    public void checkRead(CaseFolder caseFolder, AuthenticatedUser user) {
        if (user.hasRole(ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        if (user.hasRole(ROLE_CASE_FOLDER_READ) && (caseFolder.getOwner().equals(user.username()))) {
            return;
        }
        throw new SecurityException("User " + user.username() + " is not allowed to read case folder " + caseFolder.getId());
    }

    @Override
    public void checkWrite(CaseFolder caseFolder, AuthenticatedUser user) {
        if (user.hasRole(ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        if (user.hasRole(ROLE_CASE_FOLDER_WRITE) && (caseFolder.getOwner().equals(user.username()))) {
            return;
        }
        throw new SecurityException("User " + user.username() + " is not allowed to write case folder " + caseFolder.getId());
    }

    @Override
    public void checkCreate(AuthenticatedUser user) {
        if (user.hasRole(ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        if (user.hasRole(ROLE_CASE_FOLDER_WRITE)) {
            return;
        }
        throw new SecurityException("User " + user.username() + " is not allowed to create case folders");
    }

}
