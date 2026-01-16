package org.labcabrera.sample.archetype.casestep.infrastructure.messaging;

import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepEventBusPort;
import org.labcabrera.sample.archetype.casestep.domain.events.CaseStepCreatedEvent;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.infrastructure.messaging.kafka.StreamBridgeEventBusAdapter;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link CaseStepEventBusPort} based on
 * {@link StreamBridgeEventBusAdapter}.
 */
@Component
public class StreamBridgeCaseStepEventBusAdapter
    extends StreamBridgeEventBusAdapter
    implements CaseStepEventBusPort {

    public StreamBridgeCaseStepEventBusAdapter(StreamBridge streamBridge, SecurityPort securityPort) {
        super(streamBridge, securityPort);
    }

    @Override
    public void publish(CaseStepCreatedEvent event) {
        sendNotification("caseStepCreated-out-0", event);
    }

}
