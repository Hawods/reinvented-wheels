package com.github.hawods.context;

/**
 * @author hawods
 * @version 2023-04-22
 */
public interface ApplicationContext {
    <T> T getBean(Class<T> type);
}
