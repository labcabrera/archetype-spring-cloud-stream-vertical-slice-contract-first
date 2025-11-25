package org.labcabrera.sample.archetype.casefolder.interfaces.kafka;

import java.util.function.Consumer;

import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.UpdateCaseFolderStatusCommand;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolderStatus;
import org.labcabrera.sample.archetype.casestep.domain.events.CaseStepCreatedEvent;
import org.labcabrera.sample.archetype.shared.application.CommandBus;
import org.labcabrera.sample.archetype.shared.infrastructure.messaging.kafka.AuthenticatedConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaCaseFolderController extends AuthenticatedConsumer {

    private final CommandBus commandBus;

    @Bean
    public Consumer<Message<CreateCaseFolderCommand>> onCaseFolderCreation() {
        return command -> {
            log.debug("Received case folder creation command: {}", command.getPayload().idCardNumber());
            try {
                loadUserContext(command);
                commandBus.dispatch(command);
            }
            finally {
                SecurityContextHolder.clearContext();
            }
        };
    }

    @Bean
    public Consumer<Message<CaseStepCreatedEvent>> onCaseStepCreated() {
        return event -> {
            log.debug("Received case step created event: {}", event.getPayload().caseFolderId());
            try {
                loadUserContext(event);
                var command = new UpdateCaseFolderStatusCommand(
                    event.getPayload().caseFolderId(),
                    CaseFolderStatus.ACTIVE);
                commandBus.dispatch(command);
            }
            finally {
                SecurityContextHolder.clearContext();
            }
        };
    }
}
