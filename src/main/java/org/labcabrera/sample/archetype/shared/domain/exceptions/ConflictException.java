package org.labcabrera.sample.archetype.shared.domain.exceptions;

public class ConflictException extends DomainException {

    public ConflictException(String code, Object... args) {
        super(code, 409, args);
    }

}
