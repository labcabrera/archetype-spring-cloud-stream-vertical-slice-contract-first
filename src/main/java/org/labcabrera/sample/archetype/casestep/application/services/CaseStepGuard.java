package org.labcabrera.sample.archetype.casestep.application.services;

import static org.labcabrera.sample.archetype.casefolder.application.services.CaseFolderGuard.ROLE_CASE_FOLDER_MANAGEMENT;
import static org.labcabrera.sample.archetype.casefolder.application.services.CaseFolderGuard.ROLE_CASE_FOLDER_READ;
import static org.labcabrera.sample.archetype.casefolder.application.services.CaseFolderGuard.ROLE_CASE_FOLDER_WRITE;

import org.labcabrera.sample.archetype.casestep.domain.CaseStep;
import org.labcabrera.sample.archetype.shared.application.Guard;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.springframework.stereotype.Component;

/**
 * Basic implementation of {@link Guard} for {@link CaseStep} using roles defined in
 * {@link AuthenticatedUser}.
 */
@Component
public class CaseStepGuard implements Guard<CaseStep> {

    private static final String ERR_TEMPLATE_WITH_ID = "User %s is not allowed to %s case step %s";
    private static final String ERR_TEMPLATE = "User %s is not allowed to create case folders";

    @Override
    public void checkRead(CaseStep caseStep, AuthenticatedUser user) {
        if (user.hasRole(ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        else if (user.hasRole(ROLE_CASE_FOLDER_READ) && (caseStep.getOwner().equals(user.username()))) {
            return;
        }
        throw new SecurityException(String.format(ERR_TEMPLATE_WITH_ID, user.username(), "read", caseStep.getId()));
    }

    @Override
    public void checkWrite(CaseStep caseStep, AuthenticatedUser user) {
        if (user.hasRole(ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        else if (user.hasRole(ROLE_CASE_FOLDER_WRITE) && (caseStep.getOwner().equals(user.username()))) {
            return;
        }
        throw new SecurityException(String.format(ERR_TEMPLATE_WITH_ID, user.username(), "write", caseStep.getId()));
    }

    @Override
    public void checkCreate(AuthenticatedUser user) {
        if (user.hasRole(ROLE_CASE_FOLDER_MANAGEMENT)) {
            return;
        }
        else if (user.hasRole(ROLE_CASE_FOLDER_WRITE)) {
            return;
        }
        throw new SecurityException(String.format(ERR_TEMPLATE, user.username()));
    }

}
