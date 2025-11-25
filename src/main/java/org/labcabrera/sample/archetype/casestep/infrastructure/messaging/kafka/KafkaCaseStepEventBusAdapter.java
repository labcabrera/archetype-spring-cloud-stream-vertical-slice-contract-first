package org.labcabrera.sample.archetype.casestep.infrastructure.messaging.kafka;

import org.labcabrera.sample.archetype.casestep.application.ports.CaseStepEventBusPort;
import org.labcabrera.sample.archetype.casestep.domain.events.CaseStepCreatedEvent;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.infrastructure.messaging.kafka.StreamBridgeEventBusAdapter;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaCaseStepEventBusAdapter
    extends StreamBridgeEventBusAdapter
    implements CaseStepEventBusPort {

    public KafkaCaseStepEventBusAdapter(StreamBridge streamBridge, SecurityPort securityPort) {
        super(streamBridge, securityPort);
    }

    @Override
    public void publish(CaseStepCreatedEvent event) {
        sendNotification("caseStepCreated-out-0", event);
    }

}
