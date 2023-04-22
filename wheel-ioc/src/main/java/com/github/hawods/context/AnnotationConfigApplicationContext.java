package com.github.hawods.context;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hawods
 * @version 2023-04-22
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    private final Map<String, Object> beanMap = new HashMap<>();

    public AnnotationConfigApplicationContext(Object root) throws URISyntaxException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String basePackage = root.getClass().getPackageName();
        ResourceResolver resourceResolver = new SingletonResourceResolver();
        Class<?>[] types = resourceResolver.resolve(basePackage);
        for (Class<?> type : types) {
            beanMap.put(type.getName(), type.getDeclaredConstructor().newInstance());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> type) {
        for (Object value : beanMap.values()) {
            if (type.isAssignableFrom(value.getClass())) {
                return (T) value;
            }
        }
        return null;
    }
}
