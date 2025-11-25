package org.labcabrera.sample.archetype.shared.application;

public interface QueryBus {

    <R> R dispatch(Object query);

}
