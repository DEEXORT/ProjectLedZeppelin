package com.quest.config;

import com.quest.repository.Repository;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLocator {
//    private static final Map<Class<?>, Object> components = new ConcurrentHashMap<>();
    private static final Map<String, Object> components = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
//    @SneakyThrows
//    public static <T> T getService(final Class<T> serviceClass) {
//        if (components.containsKey(serviceClass)) {
//            return (T) components.get(serviceClass); // Если компонент проинициализирован, возвращаем его.
//        } else {
//            // Иначе создаем экземпляр класса
//            Constructor<?> constructor = serviceClass.getConstructors()[0]; // Получаем его конструктор
//            Class<?>[] parameterTypes = constructor.getParameterTypes(); // Получаем параметры конструктора
//            Object[] parameters = new Object[parameterTypes.length]; // Создаем массив пустых объектов для параметров
//            for (int i = 0; i < parameterTypes.length; i++) {
//                parameters[i] = ServiceLocator.getService(parameterTypes[i]); // Ищем рекурсивно компоненты
//            }
//            Object newInstance = constructor.newInstance(parameters); // Создаем экземпляр
//            components.put(serviceClass, newInstance); // Сохраняем в контейнер компонентов
//            return (T) newInstance;
//        }
//    }

    @SneakyThrows
    public static <T> T getService(final Class<T> serviceClass, final Class<?>... genericTypes) {
        String key = generateKey(serviceClass, genericTypes); // Имя параметризированного класса (например, Repository<User>)

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

}
