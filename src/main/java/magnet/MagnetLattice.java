package magnet;

import main.Simulation;
import neighbor.Level1NeighborCalculation;
import neighbor.NeighborCalculation;
import parameters.LatticeParametersImpl;
import point.Point;
import processor.ParameterProcessor;
import processor.ParameterResult;

import java.util.*;
import java.util.stream.Collectors;

public class MagnetLattice {

    private Magnet[][] magnets;
    private MagnetLatticeMemento lastState;
    private NeighborCalculation neighborCalculation;
    private Simulation.LatticeParameters latticeParameters;
    private ParameterProcessor parameterProcessor;

    private MagnetLattice(int[][] lattice, ParameterProcessor parameterProcessor, int states) {

        this.parameterProcessor = parameterProcessor;

        this.neighborCalculation = new Level1NeighborCalculation(lattice.length, lattice[0].length);

        Magnet[][] magnets = new Magnet[lattice.length][lattice[0].length];

        for (int x = 0; x < lattice.length; x++) {
            for (int y = 0; y < lattice[x].length; y++) {

                magnets[x][y] = new Magnet(lattice[x][y], states);

            }
        }

        for (int x = 0; x < lattice.length; x++) {
            for (int y = 0; y < lattice[x].length; y++) {


            Map<Integer, List<Point>> neighbors = neighborCalculation.addNeighbors(new HashMap<>(), x, y);

            Map<Integer, List<Magnet>> neighborsAsMagnets =  neighbors.entrySet().stream()
                    .collect(
                            Collectors.toMap(
                                    a-> a.getKey(),
                                    b -> b.getValue().stream().map(point -> magnets[point.getX()][point.getY()]).collect(Collectors.toList())
                            )
                    );

                magnets[x][y].setNeighbors(neighborsAsMagnets);
            }
        }


        this.magnets = magnets;
        calculateParameters();

    }

    public Simulation.LatticeParameters getLatticeParameters() {
        return latticeParameters;
    }

    public Magnet[][] getMagnets() {
        return magnets;
    }

    private int[][] asLattice() {
        int[][] lattice = new int[magnets.length][magnets[0].length];

        for (int x = 0; x < magnets.length; x++) {
            for (int y = 0; y < magnets[x].length; y++) {

                lattice[x][y] = magnets[x][y].getState();

            }
        }

        return lattice;
    }

    public double applyChanges(Set<Point> pointsToChange) {

        Set<Magnet> magnetsToChange = pointsToChange.stream().map(point -> magnets[point.getX()][point.getY()]).collect(Collectors.toSet());
        this.lastState = new MagnetLatticeMemento(this, magnetsToChange, this.latticeParameters);

        for (Magnet magnet : magnetsToChange) {
            magnet.changeState();
        }

        calculateParameters();

        return this.latticeParameters.totalEnergy() - lastState.latticeParameters.totalEnergy();
    }

    public void undoChanges() {
        this.lastState.restore();
    }



    private void calculateParameters() {
        ParameterResult result = parameterProcessor.process(this.magnets);
        this.latticeParameters = new LatticeParametersImpl(asLattice(), result.getTotalEnergy(), result.getOrder(), result.getNearestNeighborOrder());
    }

    public static class Builder {
        private int[][] lattice;
        private ParameterProcessor parameterProcessor;
        private int states;

        public void setLattice(int[][] lattice) {
            this.lattice = lattice;
        }

        public void setParameterProcessor(ParameterProcessor parameterProcessor) {
            this.parameterProcessor = parameterProcessor;
        }

        public void setStates(int states) {
            this.states = states;
        }

        public MagnetLattice build() {
            return new MagnetLattice(lattice, parameterProcessor, states);
        }
    }

    private class MagnetLatticeMemento {

        private MagnetLattice magnetLattice;
        private final Simulation.LatticeParameters latticeParameters;

        private Set<Magnet> magnets;

        public MagnetLatticeMemento(MagnetLattice magnetLattice, Set<Magnet> magnetsToChange, Simulation.LatticeParameters latticeParameters) {
            this.magnetLattice = magnetLattice;
            this.magnets = magnetsToChange;
            this.latticeParameters = latticeParameters;
        }


        public void restore() {
            this.magnets.stream().forEach(Magnet::restoreState);
            this.magnetLattice.latticeParameters = latticeParameters;
        }

    }

}
