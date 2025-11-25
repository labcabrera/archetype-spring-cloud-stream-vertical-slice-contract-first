package org.labcabrera.sample.archetype.casefolder.application.ports;

import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderDeletedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderUpdatedEvent;

public interface CaseFolderEventBusPort {

    void publish(CaseFolderCreatedEvent event);

    void publish(CaseFolderUpdatedEvent event);

    void publish(CaseFolderDeletedEvent event);
}
