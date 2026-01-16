package org.labcabrera.sample.archetype.shared.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfiguration {

    @Bean
    @Primary
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

    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var ptv = BasicPolymorphicTypeValidator
            .builder()
            .allowIfSubType("org.labcabrera.sample.archetype")
            .build();
        objectMapper.activateDefaultTyping(ptv, DefaultTyping.NON_FINAL, As.PROPERTY);
        return objectMapper;
    }

}
