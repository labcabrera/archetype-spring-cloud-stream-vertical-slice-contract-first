package org.labcabrera.sample.archetype.shared.application;

public interface CommandBus {

    <R> R dispatch(Object command);

}
