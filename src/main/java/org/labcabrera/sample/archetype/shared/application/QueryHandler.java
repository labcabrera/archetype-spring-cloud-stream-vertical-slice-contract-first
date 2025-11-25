package org.labcabrera.sample.archetype.shared.application;

public interface QueryHandler<Q, R> {

    R handle(Q query);

}
