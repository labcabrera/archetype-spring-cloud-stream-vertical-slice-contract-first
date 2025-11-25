package org.labcabrera.sample.archetype.shared.domain.exceptions;

public class BadRequestException extends DomainException {

    public BadRequestException(String code, Object... args) {
        super(code, 400, args);
    }

    public BadRequestException(String code, Throwable cause) {
        super(code, 400, cause);
    }

    public BadRequestException(String code, Throwable cause, Object... args) {
        super(code, 400, cause, args);
    }

}
