package com.github.hawods.context

import com.github.hawods.test.Application
import com.github.hawods.test.ioc.A
import com.github.hawods.test.ioc.B
import com.github.hawods.test.ioc.C
import com.github.hawods.test.singleton.Singleton
import com.github.hawods.test.singleton.SingletonA
import spock.lang.Specification

/**
 * @author hawods
 * @version 2023-04-22
 */
class AnnotationConfigApplicationContextTest extends Specification {
    def "GetBeanNull"() {
        given:
        def context = new AnnotationConfigApplicationContext(this)

        when:
        def obj = context.getBean(Singleton.class)

        then:
        obj == null
    }

    def "GetBeanByType"() {
        given:
        def context = new AnnotationConfigApplicationContext(new Application())

        when:
        def obj = context.getBean(Singleton.class)

        then:
        obj != null
    }

    def "GetBeanByName"() {
        given:
        def context = new AnnotationConfigApplicationContext(new Application())

        when:
        def obj = context.getBean("singletonA")

        then:
        obj != null
        obj.getClass() == SingletonA.class
    }

    def "InjectByMethod"() {
        given:
        def context = new AnnotationConfigApplicationContext(new Application())

        when:
        def b = context.getBean(B.class)

        then:
        b != null
        b.getA() != null
        b.getA().class == A.class
    }

    def "InjectByField"() {
        given:
        def context = new AnnotationConfigApplicationContext(new Application())

        when:
        def c = context.getBean(C.class)

        then:
        c != null
        c.getB() != null
        c.getB().class == B.class
        c.getB().getA() != null
        c.getB().getA().class == A.class
    }
}
