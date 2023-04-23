package com.github.hawods.context;

import com.github.hawods.ioc.Metadata;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author hawods
 * @version 2023-04-22
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    /**
     * 已完成初始化的Bean
     */
    private final Map<String, Object> beanMap = new HashMap<>();
    /**
     * 未完成初始化的Bean元信息
     */
    private final Map<String, Metadata> earlyBeanMetadata = new HashMap<>();

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
            if (data.injectFields().length == 0 && data.injectMethods().length == 0) {
                // TODO 未处理无默认构造函数的请求
                beanMap.put(data.beanName(), type.getDeclaredConstructor().newInstance());
            } else {
                earlyBeanMetadata.put(data.beanName(), data);
            }
        }
        // 处理依赖注入
        while (earlyBeanMetadata.size() > 0) {
            int countBefore = earlyBeanMetadata.size();
            Set<String> doneKeys = new HashSet<>();
            for (Map.Entry<String, Metadata> entry : earlyBeanMetadata.entrySet()) {
                Map<Field, Object> fieldMap = new HashMap<>();
                Map<Method, Object> methodMap = new HashMap<>();
                boolean flag = true;
                for (Field field : entry.getValue().injectFields()) {
                    Object bean = this.getBean((Class<?>) field.getGenericType());
                    if (bean == null) {
                        flag = false;
                        break;
                    }
                    fieldMap.put(field, bean);
                }
                if (!flag) {
                    break;
                }
                for (Method method : entry.getValue().injectMethods()) {
                    // TODO 只支持单个参数的方法注入
                    Object bean = this.getBean(method.getParameterTypes()[0]);
                    if (bean == null) {
                        flag = false;
                        break;
                    }
                    methodMap.put(method, bean);
                }
                if (flag) {
                    Object bean = entry.getValue().beanClass().getDeclaredConstructor().newInstance();
                    for (Map.Entry<Field, Object> item : fieldMap.entrySet()) {
                        item.getKey().setAccessible(true);
                        item.getKey().set(bean, item.getValue());
                    }
                    for (Map.Entry<Method, Object> item : methodMap.entrySet()) {
                        item.getKey().invoke(bean, item.getValue());
                    }
                    beanMap.put(entry.getValue().beanName(), bean);
                    doneKeys.add(entry.getKey());
                }
            }
            doneKeys.forEach(earlyBeanMetadata::remove);
            int countAfter = earlyBeanMetadata.size();
            if (countAfter == countBefore) {
                throw new RuntimeException("存在循环依赖");
            }
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
