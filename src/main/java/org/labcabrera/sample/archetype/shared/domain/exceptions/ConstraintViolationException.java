package org.labcabrera.sample.archetype.shared.domain.exceptions;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

@Getter
public class ConstraintViolationException extends DomainException {

    private final transient Set<? extends ConstraintViolation<?>> violations;

    public ConstraintViolationException(String code, Set<? extends ConstraintViolation<?>> violations) {
        super(code, 400);
        this.violations = violations;
    }

}
