import neighbor.Level1NeighbourCalculation
import point.Point
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
class NeighbourCalculationTest extends Specification {

    def underTest = new Level1NeighbourCalculation(5, 5)

    def "should calculate neighbors properly(center point)"() {
        given:
        def result = [:]
        when:
        underTest.addNeighbours(result, 2, 2)
        then:
        result[1] == [p(2,1), p(2,3), p(3,2), p(1,2)]
        result[2] == [p(1,3), p(3,3), p(1,1), p(3,1)]
        result[3] == [p(2,4), p(2,0), p(0,2), p(4,2)]
        result[4] == [p(1,4), p(3,4), p(0,3), p(4,3), p(0,1), p(4,1), p(1,0), p(3,0)]
        result[5] == [p(0,4), p(4,4), p(0,0), p(4,0)]
    }

    def "should calculate neighbors properly(left upper)"() {
        given:
        def result = [:]
        when:
        underTest.addNeighbours(result, 0, 0)
        then:
        result[1] == [p(0,4), p(0,1), p(1,0), p(4,0)]
        result[2] == [p(4,1), p(1,1), p(4,4), p(1,4)]
        result[3] == [p(0,2), p(0,3), p(3,0), p(2,0)]
        result[4] == [p(4,2), p(1,2), p(3,1), p(2,1), p(3,4), p(2,4), p(4,3), p(1,3)]
        result[5] == [p(3,2), p(2,2), p(3,3), p(2,3)]
    }

    def "should calculate neighbors properly(right lower)"() {
        given:
        def result = [:]
        when:
        underTest.addNeighbours(result,4,4)

        then:
        result[1] == [p(4,3), p(4,0), p(0,4), p(3,4)]
        result[2] == [p(3,0), p(0,0), p(3,3), p(0,3)]
        result[3] == [p(4,1), p(4,2), p(2,4), p(1,4)]
        result[4] == [p(3,1), p(0,1), p(2,0), p(1,0), p(2,3), p(1,3), p(3,2), p(0,2)]
        result[5] == [p(2,1), p(1,1), p(2,2), p(1,2)]
    }

    def p(x,y) {
        return new Point(x,y)
    }

}
