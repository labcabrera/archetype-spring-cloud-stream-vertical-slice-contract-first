package org.labcabrera.sample.archetype.casestep.application.services;

import org.labcabrera.sample.archetype.casefolder.application.services.CaseFolderGuard;
import org.labcabrera.sample.archetype.casestep.domain.CaseStep;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class CaseStepGuard implements Guard<CaseStep> {

    @Override
    public void checkRead(CaseStep caseStep, AuthenticatedUser user) {
        if (user.hasRole(CaseFolderGuard.ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        if (user.hasRole(CaseFolderGuard.ROLE_CASE_FOLDER_READ) && (caseStep.getOwner().equals(user.username()))) {
            return;
        }
        throw new SecurityException("User " + user.username() + " is not allowed to read case step " + caseStep.getId());
    }

    @Override
    public void checkWrite(CaseStep caseStep, AuthenticatedUser user) {
        if (user.hasRole(CaseFolderGuard.ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        if (user.hasRole(CaseFolderGuard.ROLE_CASE_FOLDER_WRITE) && (caseStep.getOwner().equals(user.username()))) {
            return;
        }
        throw new SecurityException("User " + user.username() + " is not allowed to write case step " + caseStep.getId());
    }

    @Override
    public void checkCreate(AuthenticatedUser user) {
        if (user.hasRole(CaseFolderGuard.ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        if (user.hasRole(CaseFolderGuard.ROLE_CASE_FOLDER_WRITE)) {
            return;
        }
        throw new SecurityException("User " + user.username() + " is not allowed to create case folders");
    }

}
