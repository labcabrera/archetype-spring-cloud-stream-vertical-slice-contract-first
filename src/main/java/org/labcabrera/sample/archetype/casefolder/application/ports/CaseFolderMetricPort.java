package org.labcabrera.sample.archetype.casefolder.application.ports;

public interface CaseFolderMetricPort {

    void incrementCaseFolderCreatedCounter();

    void incrementCaseFolderUpdatedCounter();

    void incrementCaseFolderDeletedCounter();

}
