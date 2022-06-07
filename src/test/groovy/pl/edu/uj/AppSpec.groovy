package pl.edu.uj

import spock.lang.Specification

class AppSpec extends Specification {

    def underTest = new App()

    def "should return proper value"() {
        given:
            def a = 1
            def b = 3
            def expectedResult = 4
        when:
            def result = underTest.caluclate(a, b)
        then:
            result == expectedResult
    }

}