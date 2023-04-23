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
public class B {
    private A a;

    public A getA() {
        return a;
    }

    @Inject
    public void setA(A a) {
        this.a = a;
    }
}
