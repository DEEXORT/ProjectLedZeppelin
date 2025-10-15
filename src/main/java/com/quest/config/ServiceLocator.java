package com.quest.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceLocator {
    private static final Map<String, Object> components = new ConcurrentHashMap<>();

    @SneakyThrows
    public static <T> T getService(final Class<T> serviceClass, final Class<?>... genericTypes) {
        String key = generateKey(serviceClass, genericTypes); // Parametrized type class (For example, Repository<User>)
        log.info("Get service {} with generic type {}", serviceClass, genericTypes);

        if (components.containsKey(key)) {
            return (T) components.get(key);
        }

        synchronized (ServiceLocator.class) {
            if (components.containsKey(key)) {
                return (T) components.get(key);
            }

            Constructor<?> constructor = serviceClass.getConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i] == Class.class) {
                    parameters[i] = genericTypes[0]; // Первый generic тип
                } else {
                    parameters[i] = getService(parameterTypes[i]);
                }
            }

            T instance = (T) constructor.newInstance(parameters);
            components.put(key, instance);
            return instance;
        }
    }

    // ServiceLocator.getService(Repository.class, User.class)
    private static String generateKey(Class<?> serviceClass, Class<?>... genericTypes) {
        if (genericTypes.length == 0) {
            return serviceClass.getName();
        }
        return serviceClass.getName() + "<" + genericTypes[0].getName() + ">";
    }

    public static void clear() {
        components.clear();
    }
}
