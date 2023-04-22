package com.github.hawods.context;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author hawods
 * @version 2023-04-22
 */
public interface ResourceResolver {
    Class<?>[] resolve(String basePackage) throws URISyntaxException, IOException;
}
