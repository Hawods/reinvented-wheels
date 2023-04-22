import spock.lang.Specification

/**
 * @author hawods
 * @version 2023-04-22
 */
class Test extends Specification {
    def "test"() {
        given:
        def a = 1
        def b = 1
        when:
        def c = a + b
        then:
        c == 2
    }
}
