package magnet;

import main.Simulation;
import neighbor.Level1NeighbourCalculation;
import neighbor.NeighbourCalculation;
import parameters.LatticeParametersImpl;
import point.Point;
import processor.ParameterProcessor;
import processor.ParameterResult;
import util.Tuple;

import java.util.*;
import java.util.stream.Collectors;

public class MagnetLattice {

    private Magnet[][] magnets;
    private MagnetLatticeMemento lastState;
    private NeighbourCalculation neighbourCalculation;
    private Simulation.LatticeParameters latticeParameters;
    private ParameterProcessor parameterProcessor;

    private MagnetLattice(int[][] lattice, ParameterProcessor parameterProcessor, int states) {
        this.parameterProcessor = parameterProcessor;
        this.neighbourCalculation = new Level1NeighbourCalculation(lattice.length, lattice[0].length);

        Magnet[][] magnetLattice = new Magnet[lattice.length][lattice[0].length];

        for (int x = 0; x < lattice.length; x++) {
            for (int y = 0; y < lattice[x].length; y++) {
                magnetLattice[x][y] = new Magnet(lattice[x][y], states);
            }
        }

        for (int x = 0; x < lattice.length; x++) {
            for (int y = 0; y < lattice[x].length; y++) {
            Map<Integer, List<Point>> neighbors = neighbourCalculation.addNeighbours(new HashMap<>(), x, y);
            Map<Integer, List<Magnet>> neighborsAsMagnets =  neighbors.entrySet().stream()
                    .collect(
                            Collectors.toMap(
                                    Map.Entry::getKey,
                                    b -> b.getValue().stream().map(point -> magnetLattice[point.getX()][point.getY()]).collect(Collectors.toList())
                            )
                    );
                magnetLattice[x][y].setNeighbours(neighborsAsMagnets);
            }
        }

        this.magnets = magnetLattice;
        calculateParameters();
    }

    public Simulation.LatticeParameters getLatticeParameters() {
        return latticeParameters;
    }

    public Tuple<Integer, Integer> getShape() {
        return new Tuple<>(magnets.length, magnets[0].length);
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

    private int[][] asLattice() {
        int[][] lattice = new int[magnets.length][magnets[0].length];

        for (int x = 0; x < magnets.length; x++) {
            for (int y = 0; y < magnets[x].length; y++) {

                lattice[x][y] = magnets[x][y].getState();

            }
        }

        return lattice;
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

        private final MagnetLattice magnetLattice;
        private final Simulation.LatticeParameters latticeParameters;

        private final Set<Magnet> magnets;

        private MagnetLatticeMemento(MagnetLattice magnetLattice, Set<Magnet> magnetsToChange, Simulation.LatticeParameters latticeParameters) {
            this.magnetLattice = magnetLattice;
            this.magnets = magnetsToChange;
            this.latticeParameters = latticeParameters;
        }


        private void restore() {
            this.magnets.stream().forEach(Magnet::restoreState);
            this.magnetLattice.latticeParameters = latticeParameters;
        }

    }

}
