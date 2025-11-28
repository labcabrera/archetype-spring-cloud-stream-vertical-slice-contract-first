package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.labcabrera.sample.archetype.generated.client.user.ApiClient;
import org.labcabrera.sample.archetype.generated.client.user.api.UsersApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserApiClientConfiguration {

    @Bean
    public ApiClient usersApiClient(ObjectMapper objectMapper,
        @Value("${users.client.base-path:http://localhost:8083}") String basePath) {
        ApiClient apiClient = new ApiClient();
        apiClient.setObjectMapper(objectMapper);
        apiClient.setBasePath(basePath);
        return apiClient;
    }

    @Bean
    public UsersApi usersApi(ApiClient apiClient) {
        return apiClient.buildClient(UsersApi.class);
    }
}
