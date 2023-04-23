package com.github.hawods.test.ioc;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * reinvented-wheels
 *
 * @author hawods
 * @version 2023/4/23
 */
@Singleton
public class C {
    @Inject
    private B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
