package org.labcabrera.sample.archetype.casefolder.infrastructure.metrics;

import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderMetricPort;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Adapter for case folder metrics using Micrometer.
 */
@Component
public class CaseFolderMetricAdapter implements CaseFolderMetricPort {

    private final Counter caseFolderCreatedCounter;
    private final Counter caseFolderUpdatedCounter;
    private final Counter caseFolderDeletedCounter;

    public CaseFolderMetricAdapter(MeterRegistry meterRegistry) {
        caseFolderCreatedCounter = Counter.builder("casefoldercreated")
            .description("Number of case folders created")
            .register(meterRegistry);
        caseFolderUpdatedCounter = Counter.builder("casefolderupdated")
            .description("Number of case folders updated")
            .register(meterRegistry);
        caseFolderDeletedCounter = Counter.builder("casefolderdeleted")
            .description("Number of case folders deleted")
            .register(meterRegistry);
    }

    @Override
    public void incrementCaseFolderCreatedCounter() {
        caseFolderCreatedCounter.increment();
    }

    @Override
    public void incrementCaseFolderUpdatedCounter() {
        caseFolderUpdatedCounter.increment();
    }

    @Override
    public void incrementCaseFolderDeletedCounter() {
        caseFolderDeletedCounter.increment();
    }
}
