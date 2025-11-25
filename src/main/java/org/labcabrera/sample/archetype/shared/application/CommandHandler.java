package org.labcabrera.sample.archetype.shared.application;

public interface CommandHandler<C, R> {

    R handle(C command);

}
