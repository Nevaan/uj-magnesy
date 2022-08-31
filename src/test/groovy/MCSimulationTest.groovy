import magnet.Magnet
import main.Simulation
import neighbor.Level1NeighborCalculation
import point.Point
import spock.lang.Ignore
import spock.lang.IgnoreRest
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
        underTest.setLattice(lattice, 8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countTotalEnergy(toMagnetLattice(lattice))
        then:
        (result / (4 * 4)) == -2
    }

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
        underTest.setLattice(lattice, 8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countTotalEnergy(toMagnetLattice(lattice))
        then:
        (result / (4 * 4)) == 2
    }

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
        underTest.setLattice(lattice, 2)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countTotalEnergy(toMagnetLattice(lattice))
        then:
//        println (result / (4 * 4))
        true
    }

    def "orderParam test 1"() {
        given:
        // 8 wartosci kierunku
        def lattice = [
                [new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:])],
                [new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:])],
                [new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:])],
                [new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:])]
        ] as Magnet[][]
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
                [new Magnet(2, [:]), new Magnet(6, [:]), new Magnet(2, [:]), new Magnet(6, [:])],
                [new Magnet(6, [:]), new Magnet(2, [:]), new Magnet(6, [:]), new Magnet(2, [:])],
                [new Magnet(2, [:]), new Magnet(6, [:]), new Magnet(2, [:]), new Magnet(6, [:])],
                [new Magnet(6, [:]), new Magnet(2, [:]), new Magnet(6, [:]), new Magnet(2, [:])]
        ] as Magnet[][]
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
                [new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(6, [:]), new Magnet(6, [:])],
                [new Magnet(6, [:]), new Magnet(2, [:]), new Magnet(6, [:]), new Magnet(2, [:])],
                [new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(6, [:])],
                [new Magnet(6, [:]), new Magnet(2, [:]), new Magnet(2, [:]), new Magnet(2, [:])]
        ] as Magnet[][]
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
        underTest.setLattice(lattice, 8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countNearestNeighbourOrder(toMagnetLattice(lattice))
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
        underTest.setLattice(lattice, 8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countNearestNeighbourOrder(toMagnetLattice(lattice))
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
        underTest.setLattice(lattice, 8)
        underTest.setEnergyParameters([0.0 as Double, 1.0 as Double], 0.0)
        def result = underTest.countNearestNeighbourOrder(toMagnetLattice(lattice))
        then:
        println result
    }

    Magnet[][] toMagnetLattice(int[][] lattice) {
        Magnet[][] magnetArray = new Magnet[lattice.length][lattice.length]
        for (int i = 0; i < lattice.length; i++) {
            for (int j = 0; j < lattice.length; j++) {

                magnetArray[i][j] = new Magnet(lattice[i][j], new Level1NeighborCalculation(lattice.length, lattice[0].length).addNeighbors([:], i, j))

            }
        }
        return magnetArray
    }
}
