package com.github.hawods.context

import com.github.hawods.test.singleton.SingletonA
import com.github.hawods.test.singleton.SingletonB
import spock.lang.Specification

/**
 * @author hawods
 * @version 2023-04-22
 */
class MetadataResourceResolverTest extends Specification {
    def "Resolve"() {
        given:
        def basePackage = "com.github.hawods.test.singleton"
        def resolver = new MetadataResourceResolver()

        when:
        def resources = resolver.resolve(basePackage);

        then:
        resources.length == 2

        when:
        def a = resources.find(it -> it.beanClass() == SingletonA.class)
        def b = resources.find(it -> it.beanClass() == SingletonB.class)

        then:
        a != null
        b != null
        a.beanName() == "singletonA"
        b.beanName() == "b"
    }
}
