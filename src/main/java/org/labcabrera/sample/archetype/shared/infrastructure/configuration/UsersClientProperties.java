package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "users.client")
@Getter
@Setter
public class UsersClientProperties {

    /** Base path for the generated users API client. */
    private String basePath = "http://localhost:8083";

    /** authType: none | bearer | oauth-password | oauth-client-credentials */
    private String authType = "none";

    /** The name of the auth in the generated client (eg. 'oidc' or 'bearer-jwt'). */
    private String authName = "oidc";

    /** Static bearer token (optional). */
    private String bearerToken;

    private Oauth oauth = new Oauth();

    @Getter
    @Setter
    public static class Oauth {
        private String authorizationUrl;
        private String tokenUrl;
        private String clientId;
        private String clientSecret;
        private String username;
        private String password;
        private String scopes;
    }
}
