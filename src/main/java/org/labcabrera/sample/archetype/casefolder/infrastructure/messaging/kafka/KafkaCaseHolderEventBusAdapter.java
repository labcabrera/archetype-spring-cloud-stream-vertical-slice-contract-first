package org.labcabrera.sample.archetype.casefolder.infrastructure.messaging.kafka;

import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderEventBusPort;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderCreatedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderDeletedEvent;
import org.labcabrera.sample.archetype.casefolder.domain.events.CaseFolderUpdatedEvent;
import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.infrastructure.messaging.kafka.StreamBridgeEventBusAdapter;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaCaseHolderEventBusAdapter
    extends StreamBridgeEventBusAdapter
    implements CaseFolderEventBusPort {

    public KafkaCaseHolderEventBusAdapter(StreamBridge streamBridge, SecurityPort securityPort) {
        super(streamBridge, securityPort);
    }

    @Override
    public void publish(CaseFolderCreatedEvent event) {
        sendNotification("caseFolderCreated-out-0", event);
    }

    @Override
    public void publish(CaseFolderUpdatedEvent event) {
        sendNotification("caseFolderUpdated-out-0", event);
    }

    @Override
    public void publish(CaseFolderDeletedEvent event) {
        sendNotification("caseFolderDeleted-out-0", event);
    }

}
