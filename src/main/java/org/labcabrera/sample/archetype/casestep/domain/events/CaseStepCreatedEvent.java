package org.labcabrera.sample.archetype.casestep.domain.events;

public record CaseStepCreatedEvent(

    String caseStepId,

    String caseFolderId) {

}
