package org.labcabrera.sample.archetype.shared.domain.exceptions;

public class NotModifiedException extends DomainException {

    private static final String CODE = "NOT_MODIFIED";

    public NotModifiedException(String message, Object... args) {
        super(CODE, 304, message, args);
    }

}
