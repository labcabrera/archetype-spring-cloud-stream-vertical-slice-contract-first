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
public class SimpleCommandBus implements CommandBus {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();
    private final Map<String, CommandHandler<?, ?>> commandHandlerBeans;

    @PostConstruct
    public void init() {
        commandHandlerBeans.forEach((beanName, handler) -> {
            Class<?> commandClass = getCommandClass(handler);
            if (commandClass != null) {
                handlers.put(commandClass, handler);
                log.info("Auto-registered CommandHandler: {} for command: {}", beanName, commandClass.getSimpleName());
            }
            else {
                log.warn("Could not determine command type for handler: {}", beanName);
            }
        });
    }

    public <C, R> void registerHandler(Class<C> commandClass, CommandHandler<C, R> handler) {
        handlers.put(commandClass, handler);
    }

    @Override
    public <R> R dispatch(Object command) {
        log.debug("Dispatching command: {}", command.getClass().getSimpleName());
        CommandHandler<Object, R> handler = (CommandHandler<Object, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler registered for command: " + command.getClass().getName());
        }
        return handler.handle(command);
    }

    private Class<?> getCommandClass(CommandHandler<?, ?> handler) {
        Type[] genericInterfaces = handler.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();
                if (rawType.equals(CommandHandler.class)) {
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
