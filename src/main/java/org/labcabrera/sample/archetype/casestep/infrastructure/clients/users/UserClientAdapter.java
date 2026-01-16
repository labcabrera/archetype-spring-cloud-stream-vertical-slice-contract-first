package org.labcabrera.sample.archetype.casestep.infrastructure.clients.users;

import org.labcabrera.sample.archetype.casestep.application.ports.UserClientPort;
import org.labcabrera.sample.archetype.casestep.domain.StepType;
import org.labcabrera.sample.archetype.casestep.domain.exceptions.AssignedUserReadException;
import org.labcabrera.sample.archetype.generated.client.user.api.UsersApi;
import org.labcabrera.sample.archetype.generated.client.user.model.UserDto;
import org.labcabrera.sample.archetype.generated.client.user.model.UserDtoPageResponse;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link UserClientPort} using the OpenAPI generated {@link UsersApi}
 * client.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserClientAdapter implements UserClientPort {

    private final UsersApi usersApi;

    @Override
    @CircuitBreaker(name = "cbUserApi", fallbackMethod = "getAssignedUserFallback")
    public String getAssignedUser(StepType stepType) {
        log.debug("Fetching user for step type {}", stepType);
        String rsql = "name!=user-error";
        UserDto user;
        try {
            UserDtoPageResponse pagedResponse = usersApi.getByRsql(rsql, 0, 10, null);
            if (pagedResponse.getContent().isEmpty()) {
                throw new AssignedUserReadException("user.client.err.no-user-available");
            }
            user = pagedResponse.getContent().iterator().next();
            log.debug("Found user {} for step type {}", user.getName(), stepType);
            return user.getName();
        }
        catch (AssignedUserReadException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new AssignedUserReadException("user.client.err.user-api-error", ex);
        }
    }

    public String getAssignedUserFallback(StepType stepType, Throwable ex) {
        log.error("Fallback triggered when fetching user for step type {}. Throwing exception", stepType);
        throw new AssignedUserReadException("user.client.err.fallback-error", ex);
    }

}
