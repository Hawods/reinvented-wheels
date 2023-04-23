package com.github.hawods.context;

import com.github.hawods.ioc.Metadata;

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

    /**
     * 初始化容器
     *
     * @param root
     * @throws URISyntaxException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public AnnotationConfigApplicationContext(Object root) throws URISyntaxException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String basePackage = root.getClass().getPackageName();
        ResourceResolver resourceResolver = new MetadataResourceResolver();
        Metadata[] metadata = resourceResolver.resolve(basePackage);
        for (Metadata data : metadata) {
            Class<?> type = data.beanClass();
            beanMap.put(data.beanName(), type.getDeclaredConstructor().newInstance());
        }
    }

    /**
     * 获取容器里的Bean
     *
     * @param name
     * @return
     */
    @Override
    public Object getBean(String name) {
        return beanMap.get(name);
    }

    /**
     * 获取容器里的Bean
     *
     * @param type
     * @param <T>
     * @return
     */
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
