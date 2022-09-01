package magnet;

import neighbor.Level1NeighborCalculation;
import neighbor.NeighborCalculation;
import point.Point;
import processor.ParameterProcessor;
import processor.ParameterResult;

import java.util.*;
import java.util.stream.Collectors;

public class MagnetLattice {

    private Magnet[][] magnets;
    private MagnetLatticeMemento lastState;
    private NeighborCalculation neighborCalculation;
    private double totalEnergy;
    private double orderParameter;
    private double nearestNeighborOrder;

    private ParameterProcessor parameterProcessor;


    public MagnetLattice(int[][] lattice, ParameterProcessor parameterProcessor, int states) {

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
        singleLoop();

    }

    public Magnet[][] getMagnets() {
        return magnets;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }

    public double getOrderParameter() {
        return orderParameter;
    }

    public double getNearestNeighborOrder() {
        return nearestNeighborOrder;
    }

    public int[][] asLattice() {
        int[][] lattice = new int[magnets.length][magnets[0].length];

        for (int x = 0; x < magnets.length; x++) {
            for (int y = 0; y < magnets[x].length; y++) {

                lattice[x][y] = magnets[x][y].getState();

            }
        }

        return lattice;
    }

    public void undoChanges() {
        this.lastState.restore();
    }

    public double applyChanges(Set<Point> pointsToChange) {

        Set<Magnet> magnetsToChange = pointsToChange.stream().map(point -> magnets[point.getX()][point.getY()]).collect(Collectors.toSet());
        this.lastState = new MagnetLatticeMemento(this, magnetsToChange, this.totalEnergy, this.orderParameter, this.nearestNeighborOrder);

        for (Magnet magnet : magnetsToChange) {
            magnet.changeState();
        }

        singleLoop();

        return this.totalEnergy - lastState.totalEnergy;
    }



    private void singleLoop() {
        ParameterResult result = parameterProcessor.process(this.magnets);

        this.totalEnergy = result.getTotalEnergy();
        this.orderParameter = result.getOrder();
        this.nearestNeighborOrder = result.getNearestNeighborOrder();
    }

    private class MagnetLatticeMemento {

        private MagnetLattice magnetLattice;
        private final double totalEnergy;
        private final double orderParameter;
        private final double nearestNeighborOrder;

        private Set<Magnet> magnets;

        public MagnetLatticeMemento(MagnetLattice magnetLattice, Set<Magnet> magnetsToChange, double totalEnergy, double orderParameter, double nearestNeighborOrder) {
            this.magnetLattice = magnetLattice;
            this.magnets = magnetsToChange;
            this.totalEnergy = totalEnergy;
            this.orderParameter = orderParameter;
            this.nearestNeighborOrder = nearestNeighborOrder;
        }


        public void restore() {
            this.magnets.stream().forEach(Magnet::restoreState);
            this.magnetLattice.totalEnergy = totalEnergy;
            this.magnetLattice.orderParameter = orderParameter;
            this.magnetLattice.nearestNeighborOrder = nearestNeighborOrder;
        }

    }

}
