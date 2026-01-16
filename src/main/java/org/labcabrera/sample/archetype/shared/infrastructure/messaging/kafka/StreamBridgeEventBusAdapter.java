package org.labcabrera.sample.archetype.shared.infrastructure.messaging.kafka;

import java.time.Instant;
import java.util.UUID;

import org.labcabrera.sample.archetype.shared.application.SecurityPort;
import org.labcabrera.sample.archetype.shared.domain.exceptions.EventNotificationException;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for adapters that generate events through StreamBridge with custom headers.
 */
@RequiredArgsConstructor
@Slf4j
public abstract class StreamBridgeEventBusAdapter {

    protected final StreamBridge streamBridge;
    protected final SecurityPort securityPort;

    protected void sendNotification(String binding, Object event) {
        try {
            var user = securityPort.requireCurrentUser();
            String correlationId = UUID.randomUUID().toString();
            Message<Object> message = MessageBuilder
                .withPayload(event)
                .setHeader("contentType", "application/json")
                .setHeader("x-correlation-id", correlationId)
                .setHeader("x-origin", "case-service")
                .setHeader("x-sent-at", Instant.now().toString())
                .setHeader("x-username", user.username())
                .setHeader("x-roles", String.join(",", user.roles()))
                .build();
            streamBridge.send(binding, message);
        }
        catch (Exception ex) {
            throw new EventNotificationException("msg.err.event-notification", ex);
        }
    }

}
