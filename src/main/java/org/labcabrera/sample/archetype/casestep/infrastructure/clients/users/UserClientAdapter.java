package org.labcabrera.sample.archetype.casestep.infrastructure.clients.users;

import org.labcabrera.sample.archetype.casestep.application.ports.UserClientPort;
import org.labcabrera.sample.archetype.casestep.domain.StepType;
import org.labcabrera.sample.archetype.generated.client.user.api.UsersApi;
import org.labcabrera.sample.archetype.generated.client.user.model.UserDto;
import org.labcabrera.sample.archetype.generated.client.user.model.UserDtoPageResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserClientAdapter implements UserClientPort {

    private final UsersApi usersApi;

    @Override
    public String getAssignedUser(StepType stepType) {
        UserDtoPageResponse response = getByRsql("name!=ERR", 0, 10, null).orElseThrow();
        return response.getContent().get(0).getName();
    }

    public Optional<UserDto> getById(String userId) {
        try {
            return Optional.ofNullable(usersApi.getById(userId));
        }
        catch (Exception e) {
            log.warn("Failed to fetch user by id {}: {}", userId, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<UserDtoPageResponse> getByRsql(String q, Integer page, Integer size, List<String> sort) {
        try {
            return Optional.ofNullable(usersApi.getByRsql(q, page, size, sort));
        }
        catch (Exception e) {
            log.warn("Failed to fetch users by rsql '{}': {}", q, e.getMessage());
            return Optional.empty();
        }
    }

}
