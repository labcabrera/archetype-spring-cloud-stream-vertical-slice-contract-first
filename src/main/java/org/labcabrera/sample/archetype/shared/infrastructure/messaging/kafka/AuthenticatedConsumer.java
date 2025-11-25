package org.labcabrera.sample.archetype.shared.infrastructure.messaging.kafka;

import java.util.Arrays;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AuthenticatedConsumer {

    protected void loadUserContext(Message<?> message) {
        String username = message.getHeaders().get("x-username", String.class);
        String roles = message.getHeaders().get("x-roles", String.class);
        log.debug("Loading user context {} ({})", username, roles);
        if (username != null) {
            List<SimpleGrantedAuthority> authorities = (roles == null || roles.isBlank())
                ? List.of()
                : Arrays.stream(roles.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }
}
