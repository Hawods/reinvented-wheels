package com.github.hawods.context;

/**
 * @author hawods
 * @version 2023-04-22
 */
public interface ApplicationContext {
    /**
     * 获取容器里的Bean
     *
     * @param name
     * @return
     */
    Object getBean(String name);

    /**
     * 获取容器里的Bean
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> T getBean(Class<T> type);
}
