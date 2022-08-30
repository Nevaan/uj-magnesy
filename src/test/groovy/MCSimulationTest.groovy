import magnet.Magnet
import main.Simulation
import point.Point
import spock.lang.Ignore
import spock.lang.Specification

class MCSimulationTest extends Specification {

    def underTest = new MCSimulation()

    def "simTest1"() {
        given:
        def lattice = [
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        ] as int[][]
        when:
        underTest.setLattice(lattice, 4)
        underTest.setEnergyParameters([1.0 as Double, 0.0 as Double, 0.0 as Double], 0.0)
        underTest.setProbabilityFormula(Simulation.ProbabilityFormula.GLAUBER)
        underTest.setTkB(0.0000000001)
        underTest.executeMCSteps(1)
        then:
        println((underTest.getState().totalEnergy()) / (32 * 32))
    }


    def "should calculateAngle properly"() {
        given:
        underTest.setStates(states)
        when:
        def result = underTest.countAngle(angle).round(3)
        then:
        result == expected
        where:
        states | angle               | expected
        8      | new Magnet(0, null) | 0
        8      | new Magnet(1, null) | 0.785
        8      | new Magnet(2, null) | 1.571
        8      | new Magnet(3, null) | 2.356
        8      | new Magnet(4, null) | 3.142
        8      | new Magnet(5, null) | 3.927
        8      | new Magnet(6, null) | 4.712
        8      | new Magnet(7, null) | 5.498
        4      | new Magnet(0, null) | 0
        4      | new Magnet(1, null) | 1.571
        4      | new Magnet(2, null) | 3.142
        4      | new Magnet(3, null) | 4.712
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
        // 2x2
        underTest.setLattice(lattice, 100)
        underTest.setNeighborCalculation()
        def result = underTest.buildMagnets()[2][2]
        then:
        result.getNeighbors()[1] == [p(2, 1), p(2, 3), p(3, 2), p(1, 2)]
        result.getNeighbors()[2] == [p(1, 3), p(3, 3), p(1, 1), p(3, 1)]
        result.getNeighbors()[3] == [p(2, 4), p(2, 0), p(0, 2), p(4, 2)]
        result.getNeighbors()[4] == [p(1, 4), p(3, 4), p(0, 3), p(4, 3), p(0, 1), p(4, 1), p(1, 0), p(3, 0)]
        result.getNeighbors()[5] == [p(0, 4), p(4, 4), p(0, 0), p(4, 0)]
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
        underTest.setLattice(lattice, 100)
        underTest.setNeighborCalculation()
        def result = underTest.buildMagnets()[0][0]
        then:
        result.getNeighbors()[1] == [p(0,4), p(0,1), p(1,0), p(4,0)]
        result.getNeighbors()[2] == [p(4,1), p(1,1), p(4,4), p(1,4)]
        result.getNeighbors()[3] == [p(0,2), p(0,3), p(3,0), p(2,0)]
        result.getNeighbors()[4] == [p(4,2), p(1,2), p(3,1), p(2,1), p(3,4), p(2,4), p(4,3), p(1,3)]
        result.getNeighbors()[5] == [p(3,2), p(2,2), p(3,3), p(2,3)]
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
        underTest.setLattice(lattice, 100)
        underTest.setNeighborCalculation()
        def result = underTest.buildMagnets()[4][4]
        then:
        result.getNeighbors()[1] == [p(4,3), p(4,0), p(0,4), p(3,4)]
        result.getNeighbors()[2] == [p(3,0), p(0,0), p(3,3), p(0,3)]
        result.getNeighbors()[3] == [p(4,1), p(4,2), p(2,4), p(1,4)]
        result.getNeighbors()[4] == [p(3,1), p(0,1), p(2,0), p(1,0), p(2,3), p(1,3), p(3,2), p(0,2)]
        result.getNeighbors()[5] == [p(2,1), p(1,1), p(2,2), p(1,2)]
    }

    def p(x, y) {
        return new Point(x, y)
    }

    def "eTotal test 1"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2, 2, 2, 2],
                [2, 2, 2, 2],
                [2, 2, 2, 2],
                [2, 2, 2, 2]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 8)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countTotalEnergy(magnets)
        then:
        (result / (4 * 4)) == -2
    }

    def "eTotal test 2"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2, 6, 2, 6],
                [6, 2, 6, 2],
                [2, 6, 2, 6],
                [6, 2, 6, 2]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 8)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countTotalEnergy(magnets)
        then:
        (result / (4 * 4)) == 2
    }

    def "eTotal test 3"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [1, 1, 0, 0],
                [0, 1, 0, 1],
                [1, 1, 1, 0],
                [0, 1, 1, 1]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 2)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countTotalEnergy(magnets)
        then:
        println(result / (4 * 4))
    }

    def "orderParam test 1"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2, 2, 2, 2],
                [2, 2, 2, 2],
                [2, 2, 2, 2],
                [2, 2, 2, 2]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 8)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countOrderParameter(magnets)
        then:
        result == 1.0
    }

    def "orderParam test 2"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2, 6, 2, 6],
                [6, 2, 6, 2],
                [2, 6, 2, 6],
                [6, 2, 6, 2]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 8)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countOrderParameter(magnets)
        then:
        result.round(10) == 0.0
    }


    def "orderParam test 3"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2, 2, 6, 6],
                [6, 2, 6, 2],
                [2, 2, 2, 6],
                [6, 2, 2, 2]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 8)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countOrderParameter(magnets)
        then:
        println result
    }

    def "nearestNeighbourOrder test 1"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2, 2, 2, 2],
                [2, 2, 2, 2],
                [2, 2, 2, 2],
                [2, 2, 2, 2]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 8)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countNearestNeighbourOrder(magnets)
        then:
        result == 1.0
    }

    def "nearestNeighbourOrder test 2"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2, 6, 2, 6],
                [6, 2, 6, 2],
                [2, 6, 2, 6],
                [6, 2, 6, 2]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 8)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countNearestNeighbourOrder(magnets)
        then:
        result == -1.0
    }


    def "nearestNeighbourOrder test 3"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [2, 2, 6, 6],
                [6, 2, 6, 2],
                [2, 2, 2, 6],
                [6, 2, 2, 2]
        ] as int[][]
        when:
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        underTest.setLattice(lattice, 8)
        underTest.setNeighborCalculation()
        Magnet[][] magnets =  underTest.buildMagnets()
        def result = underTest.countNearestNeighbourOrder(magnets)
        then:
        println result
    }


}
