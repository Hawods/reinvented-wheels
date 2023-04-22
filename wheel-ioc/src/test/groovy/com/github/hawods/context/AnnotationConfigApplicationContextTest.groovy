package com.github.hawods.context

import com.github.hawods.test.Application
import com.github.hawods.test.singleton.Singleton
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

    def "GetBeanTest"() {
        given:
        def context = new AnnotationConfigApplicationContext(new Application())

        when:
        def obj = context.getBean(Singleton.class)

        then:
        obj != null
    }
}
