package org.labcabrera.sample.archetype.casestep.application.ports;

import org.labcabrera.sample.archetype.casestep.domain.events.CaseStepCreatedEvent;

public interface CaseStepEventBusPort {

    void publish(CaseStepCreatedEvent event);
}
