package com.github.hawods.context;

import com.github.hawods.ioc.Metadata;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author hawods
 * @version 2023-04-22
 */
public interface ResourceResolver {
    /**
     * 获取package下的所有类
     *
     * @param basePackage
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    Metadata[] resolve(String basePackage) throws URISyntaxException, IOException;
}
