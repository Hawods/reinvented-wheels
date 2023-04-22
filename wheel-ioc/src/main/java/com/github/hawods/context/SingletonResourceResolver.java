package com.github.hawods.context;

import jakarta.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hawods
 * @version 2023-04-22
 */
public class SingletonResourceResolver implements ResourceResolver {
    @Override
    public Class<?>[] resolve(String basePackage) throws URISyntaxException, IOException {
        String basePath = basePackage.replace(".", "/");
        ClassLoader classLoader = this.getClass().getClassLoader();
        Set<File> classFiles = new HashSet<>();
        Enumeration<URL> baseUrls = classLoader.getResources(basePath);
        while (baseUrls.hasMoreElements()) {
            File baseDir = new File(baseUrls.nextElement().toURI());
            classFiles.addAll(Arrays.asList(resolveFiles(baseDir)));
        }
        Class<?>[] result = classFiles.stream()
                .map(file -> {
                    String classFilePath = file.getAbsolutePath();
                    classFilePath = classFilePath.substring(classFilePath.indexOf(basePath), classFilePath.length() - 6);
                    String className = classFilePath.replace("/", ".");
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(item -> item.getDeclaredAnnotation(Singleton.class) != null)
                .toArray(Class[]::new);

        return result;
    }

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
