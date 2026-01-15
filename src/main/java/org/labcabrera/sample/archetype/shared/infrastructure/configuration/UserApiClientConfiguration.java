package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.labcabrera.sample.archetype.generated.client.user.ApiClient;
import org.labcabrera.sample.archetype.generated.client.user.api.UsersApi;
import org.labcabrera.sample.archetype.generated.client.user.auth.HttpBearerAuth;
import org.labcabrera.sample.archetype.generated.client.user.auth.OauthClientCredentialsGrant;
import org.labcabrera.sample.archetype.generated.client.user.auth.OauthPasswordGrant;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(UsersClientProperties.class)
@Slf4j
public class UserApiClientConfiguration {

    @Bean
    public ApiClient usersApiClient(ObjectMapper objectMapper, UsersClientProperties properties) {
        ApiClient apiClient = new ApiClient();
        apiClient.setObjectMapper(objectMapper);
        apiClient.setBasePath(properties.getBasePath());

        String authType = properties.getAuthType();
        if (authType != null) {
            switch (authType) {
            case "bearer": {
                String token = properties.getBearerToken();
                HttpBearerAuth bearer = new HttpBearerAuth("bearer");
                if (token != null) {
                    bearer.setBearerToken(token);
                }
                apiClient.addAuthorization(properties.getAuthName(), bearer);
                break;
            }
            case "oauth-password": {
                UsersClientProperties.Oauth o = properties.getOauth();
                OauthPasswordGrant oauth = new OauthPasswordGrant(o.getTokenUrl(), o.getScopes());
                oauth.configure(o.getUsername(), o.getPassword(), o.getClientId(), o.getClientSecret());
                apiClient.addAuthorization(properties.getAuthName(), oauth);
                apiClient.registerAccessTokenListener(
                    token -> log.info("Users client obtained access token (expiresIn={}s)", token.getExpiresIn()));
                break;
            }
            case "oauth-client-credentials": {
                UsersClientProperties.Oauth o = properties.getOauth();
                OauthClientCredentialsGrant oauth = new OauthClientCredentialsGrant(o.getAuthorizationUrl(), o.getTokenUrl(),
                    o.getScopes());
                oauth.configure(o.getClientId(), o.getClientSecret());
                apiClient.addAuthorization(properties.getAuthName(), oauth);
                apiClient.registerAccessTokenListener(
                    token -> log.info("Users client obtained access token (expiresIn={}s)", token.getExpiresIn()));
                break;
            }
            default:
                // none
            }
        }

        return apiClient;
    }

    @Bean
    public UsersApi usersApi(ApiClient apiClient) {
        return apiClient.buildClient(UsersApi.class);
    }
}
