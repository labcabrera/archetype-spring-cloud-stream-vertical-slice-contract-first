package org.labcabrera.sample.archetype.casestep.application.ports;

import org.labcabrera.sample.archetype.casestep.domain.StepType;

public interface UserClientPort {

    String getAssignedUser(StepType stepType);

}
