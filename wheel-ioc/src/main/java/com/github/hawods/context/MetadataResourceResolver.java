package com.github.hawods.context;

import com.github.hawods.ioc.Metadata;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author hawods
 * @version 2023-04-22
 */
public class MetadataResourceResolver implements ResourceResolver {
    /**
     * 获取package下所有带@Singleton注解的类
     *
     * @param basePackage
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    @Override
    public Metadata[] resolve(String basePackage) throws URISyntaxException, IOException {
        String basePath = basePackage.replace(".", "/");
        ClassLoader classLoader = this.getClass().getClassLoader();
        Set<File> classFiles = new HashSet<>();
        Enumeration<URL> baseUrls = classLoader.getResources(basePath);
        while (baseUrls.hasMoreElements()) {
            File baseDir = new File(baseUrls.nextElement().toURI());
            classFiles.addAll(Arrays.asList(resolveFiles(baseDir)));
        }
        Metadata[] result = classFiles.stream()
                .map(file -> {
                    String classFilePath = file.getAbsolutePath();
                    classFilePath = classFilePath.substring(classFilePath.indexOf(basePath), classFilePath.length() - 6);
                    String className = classFilePath.replace("/", ".");
                    try {
                        Class<?> type = Class.forName(className);
                        if (type.getDeclaredAnnotation(Singleton.class) == null) {
                            return null;
                        }
                        String beanName;
                        Named named = type.getDeclaredAnnotation(Named.class);
                        if (named != null) {
                            beanName = named.value();
                        } else {
                            beanName = type.getSimpleName().substring(0, 1).toLowerCase() + type.getSimpleName().substring(1);
                        }
                        Field[] injectFields = Arrays.stream(type.getDeclaredFields())
                                .filter(item -> item.getDeclaredAnnotation(Inject.class) != null)
                                .toArray(Field[]::new);
                        Method[] injectMethods = Arrays.stream(type.getDeclaredMethods())
                                .filter(item -> item.getDeclaredAnnotation(Inject.class) != null)
                                .toArray(Method[]::new);
                        return new Metadata(beanName, type, injectFields, injectMethods);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Metadata[]::new);

        return result;
    }

    /**
     * 递归查找当前目录下的class文件
     *
     * @param file
     * @return
     */
    private File[] resolveFiles(File file) {
        if (file.isDirectory()) {
            Set<File> subFileSet = new HashSet<>();
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                for (File subFile : subFiles) {
                    subFileSet.addAll(Arrays.asList(resolveFiles(subFile)));
                }
            }
            return subFileSet.toArray(new File[0]);
        } else if (file.isFile() && file.getName().endsWith(".class")) {
            return new File[]{file};
        }

        return new File[0];
    }
}
