import main.Simulation
import point.Point
import spock.lang.Ignore
import spock.lang.Specification

class MCSimulationTest extends Specification {




    def underTest = new MCSimulation()


    def "simTest1"() {
        given:
        def lattice = [
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        ]  as int[][]
        when:
        underTest.setLattice(lattice, 4)
        underTest.setEnergyParameters([1.0 as Double, 0.0 as Double, 0.0 as Double], 0.0)
        underTest.setProbabilityFormula(Simulation.ProbabilityFormula.GLAUBER)
        underTest.setTkB(0.0000000001)
        underTest.executeMCSteps(1)
        then:
        println ((underTest.getState().totalEnergy()) / (32*32))
    }



    def "should calculateAngle properly"() {
        given:
        underTest.setStates(states)
        when:
        def result = underTest.countAngle(angle).round(3)
        then:
        result == expected
        where:
        states | angle | expected
        8      | 0     | 0
        8      | 1     | 0.785
        8      | 2     | 1.571
        8      | 3     | 2.356
        8      | 4     | 3.142
        8      | 5     | 3.927
        8      | 6     | 4.712
        8      | 7     | 5.498
        4      | 0     | 0
        4      | 1     | 1.571
        4      | 2     | 3.142
        4      | 3     | 4.712
    }

    def "should calculate neighbors properly(center point)"() {
        given:
        def lattice = [
                [11, 12, 13, 14, 15],
                [21, 22, 23, 24, 25],
                [31, 32, 33, 34, 35],
                [41, 42, 43, 44, 45],
                [51, 52, 53, 54, 55]
        ] as int[][]
        when:
        def result = underTest.makeNeighborsCalculation(2, 2, lattice)
        then:
        result[1] == [p(2,1), p(2,3), p(3,2), p(1,2)]
        result[2] == [p(1,3), p(3,3), p(1,1), p(3,1)]
        result[3] == [p(2,4), p(2,0), p(0,2), p(4,2)]
        result[4] == [p(1,4), p(3,4), p(0,3), p(4,3), p(0,1), p(4,1), p(1,0), p(3,0)]
        result[5] == [p(0,4), p(4,4), p(0,0), p(4,0)]
    }

    def "should calculate neighbors properly(left upper)"() {
        given:
        def lattice = [
                [11, 12, 13, 14, 15],
                [21, 22, 23, 24, 25],
                [31, 32, 33, 34, 35],
                [41, 42, 43, 44, 45],
                [51, 52, 53, 54, 55]
        ] as int[][]
        when:
        def result = underTest.makeNeighborsCalculation(0, 0, lattice)
        then:
        result[1] == [p(0,4), p(0,1), p(1,0), p(4,0)]
        result[2] == [p(4,1), p(1,1), p(4,4), p(1,4)]
        result[3] == [p(0,2), p(0,3), p(3,0), p(2,0)]
        result[4] == [p(4,2), p(1,2), p(3,1), p(2,1), p(3,4), p(2,4), p(4,3), p(1,3)]
        result[5] == [p(3,2), p(2,2), p(3,3), p(2,3)]
    }

    def "should calculate neighbors properly(right lower)"() {
        given:
        def lattice = [
                [11, 12, 13, 14, 15],
                [21, 22, 23, 24, 25],
                [31, 32, 33, 34, 35],
                [41, 42, 43, 44, 45],
                [51, 52, 53, 54, 55]
        ] as int[][]
        when:
        def result = underTest.makeNeighborsCalculation(4,4, lattice)
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

    @Ignore
    def "eTotal test 1"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2,2,2,2],
                [2,2,2,2],
                [2,2,2,2],
                [2,2,2,2]
        ] as int[][]
        when:
        underTest.setStates(8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countTotalEnergy(lattice)
        then:
        (result / (4 * 4)) == -2
    }

    @Ignore
    def "eTotal test 2"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2,6,2,6],
                [6,2,6,2],
                [2,6,2,6],
                [6,2,6,2]
        ] as int[][]
        when:
        underTest.setStates(8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countTotalEnergy(lattice)
        then:
        (result / (4 * 4)) == 2
    }


    @Ignore
    def "eTotal test 3"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [1,1,0,0],
                [0,1,0,1],
                [1,1,1,0],
                [0,1,1,1]
        ] as int[][]
        when:
        underTest.setStates(2)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countTotalEnergy(lattice)
        then:
        println (result / (4 * 4))
    }

    def "orderParam test 1"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2,2,2,2],
                [2,2,2,2],
                [2,2,2,2],
                [2,2,2,2]
        ] as int[][]
        when:
        underTest.setStates(8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countOrderParameter(lattice)
        then:
        result == 1.0
    }

    def "orderParam test 2"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2,6,2,6],
                [6,2,6,2],
                [2,6,2,6],
                [6,2,6,2]
        ] as int[][]
        when:
        underTest.setStates(8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countOrderParameter(lattice)
        then:
        result.round(10) == 0.0
    }


    def "orderParam test 3"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2,2,6,6],
                [6,2,6,2],
                [2,2,2,6],
                [6,2,2,2]
        ] as int[][]
        when:
        underTest.setStates(8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countOrderParameter(lattice)
        then:
        println result
    }

    def "nearestNeighbourOrder test 1"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2,2,2,2],
                [2,2,2,2],
                [2,2,2,2],
                [2,2,2,2]
        ] as int[][]
        when:
        underTest.setStates(8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countNearestNeighbourOrder(lattice)
        then:
        result == 1.0
    }

    def "nearestNeighbourOrder test 2"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2,6,2,6],
                [6,2,6,2],
                [2,6,2,6],
                [6,2,6,2]
        ] as int[][]
        when:
        underTest.setStates(8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countNearestNeighbourOrder(lattice)
        then:
        result == -1.0
    }


    def "nearestNeighbourOrder test 3"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2,2,6,6],
                [6,2,6,2],
                [2,2,2,6],
                [6,2,2,2]
        ] as int[][]
        when:
        underTest.setStates(8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countNearestNeighbourOrder(lattice)
        then:
        println result
    }


}
