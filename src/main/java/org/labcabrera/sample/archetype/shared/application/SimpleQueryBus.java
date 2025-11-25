package org.labcabrera.sample.archetype.shared.application;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class SimpleQueryBus implements QueryBus {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();
    private final Map<String, QueryHandler<?, ?>> queryHandlerBeans;

    @PostConstruct
    public void init() {
        queryHandlerBeans.forEach((beanName, handler) -> {
            Class<?> queryClass = getQueryClass(handler);
            if (queryClass != null) {
                handlers.put(queryClass, handler);
                log.info("Auto-registered QueryHandler: {} for query: {}", beanName, queryClass.getSimpleName());
            }
            else {
                log.warn("Could not determine query type for handler: {}", beanName);
            }
        });
    }

    public <Q, R> void registerHandler(Class<Q> queryClass, QueryHandler<Q, R> handler) {
        handlers.put(queryClass, handler);
    }

    @Override
    public <R> R dispatch(Object query) {
        log.debug("Dispatching query: {}", query.getClass().getSimpleName());
        QueryHandler<Object, R> handler = (QueryHandler<Object, R>) handlers.get(query.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler registered for query: " + query.getClass().getName());
        }
        return handler.handle(query);
    }

    private Class<?> getQueryClass(QueryHandler<?, ?> handler) {
        Type[] genericInterfaces = handler.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();
                if (rawType.equals(QueryHandler.class)) {
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                        return (Class<?>) typeArguments[0];
                    }
                }
            }
        }
        // Fallback: check superclass
        Type genericSuperclass = handler.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                return (Class<?>) typeArguments[0];
            }
        }
        return null;
    }

}
