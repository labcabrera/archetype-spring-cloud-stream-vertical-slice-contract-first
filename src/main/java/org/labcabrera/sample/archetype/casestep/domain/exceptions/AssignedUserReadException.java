package org.labcabrera.sample.archetype.casestep.domain.exceptions;

import org.labcabrera.sample.archetype.shared.domain.exceptions.DomainException;

public class AssignedUserReadException extends DomainException {

    private static final int CODE = 500;

    public AssignedUserReadException(String message, Throwable cause, Object... args) {
        super(message, CODE, cause, args);
    }

    public AssignedUserReadException(String message) {
        super(message, CODE);
    }

}
