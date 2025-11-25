package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api-definition.yaml",
                    "/api-definition.json",
                    "/actuator/health",
                    "/actuator/info")
                .permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.addAll(extractRolesFromRealmAccess(jwt));
            authorities.addAll(extractScopesAuthorities(jwt));
            return authorities;
        });
        return converter;
    }

    private Collection<GrantedAuthority> extractRolesFromRealmAccess(org.springframework.security.oauth2.jwt.Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List<?> roles) {
                for (Object r : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + r.toString()));
                }
            }
        }
        return authorities;
    }

    private Collection<GrantedAuthority> extractScopesAuthorities(org.springframework.security.oauth2.jwt.Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<String> scopes = getScopesFromJwt(jwt);
        if (scopes != null) {
            for (String s : scopes) {
                authorities.add(new SimpleGrantedAuthority("SCOPE_" + s));
            }
        }
        return authorities;
    }

    private List<String> getScopesFromJwt(org.springframework.security.oauth2.jwt.Jwt jwt) {
        List<String> scopes = jwt.getClaimAsStringList("scope");
        if (scopes == null) {
            String scopeStr = jwt.getClaimAsString("scope");
            if (scopeStr != null) {
                scopes = List.of(scopeStr.split(" "));
            }
        }
        return scopes;
    }

}
