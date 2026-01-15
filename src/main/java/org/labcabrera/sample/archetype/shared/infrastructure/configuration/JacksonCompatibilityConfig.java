package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonCompatibilityConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register any available modules (JSR310, JDK8, etc.) from the classpath
        try {
            mapper.findAndRegisterModules();
        }
        catch (Exception ex) {
            // ignore: safe fallback to a plain ObjectMapper
        }
        return mapper;
    }
}
