package com.github.hawods.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * reinvented-wheels
 *
 * @author hawods
 * @version 2023/4/23
 */
public record Metadata(
        String beanName,
        Class<?> beanClass,
        Field[] injectFields,
        Method[] injectMethods
) {
}
