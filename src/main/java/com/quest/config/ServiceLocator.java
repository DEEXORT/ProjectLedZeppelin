package com.quest.config;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLocator {
    private static final Map<Class<?>, Object> components = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static <T> T getService(final Class<T> serviceClass) {
        if (components.containsKey(serviceClass)) {
            return (T) components.get(serviceClass); // Если компонент проинициализирован, возвращаем его.
        } else {
            // Иначе создаем экземпляр класса
            Constructor<?> constructor = serviceClass.getConstructors()[0]; // Получаем его конструктор
            Class<?>[] parameterTypes = constructor.getParameterTypes(); // Получаем параметры конструктора
            Object[] parameters = new Object[parameterTypes.length]; // Создаем массив пустых объектов для параметров
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = ServiceLocator.getService(parameterTypes[i]); // Ищем рекурсивно компоненты
            }
            Object newInstance = constructor.newInstance(parameters); // Создаем экземпляр
            components.put(serviceClass, newInstance); // Сохраняем в контейнер компонентов
            return (T) newInstance;
        }
    }

}
