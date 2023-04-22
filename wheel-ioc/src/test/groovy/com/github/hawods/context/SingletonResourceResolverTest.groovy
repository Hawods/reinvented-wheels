package com.github.hawods.context

import spock.lang.Specification

/**
 * @author hawods
 * @version 2023-04-22
 */
class SingletonResourceResolverTest extends Specification {
    def "Resolve"() {
        given:
        def basePackage = "com.github.hawods.test.singleton"
        def resolver = new SingletonResourceResolver()

        when:
        Class<?>[] resources = resolver.resolve(basePackage);

        then:
        resources.length == 1
    }
}
