package magnet;

import main.Simulation;
import neighbor.Level1NeighborCalculation;
import neighbor.NeighborCalculation;
import point.Point;

import java.util.*;
import java.util.stream.Collectors;

public class MagnetLattice implements Observer {

    private Magnet[][] magnets;
    private NeighborCalculation neighborCalculation;
    private Simulation.LatticeParameters parameters;
    private List<Double> externalParams;

    private int states;
    private double externalFieldAngle;

    private Set<Magnet>

    public MagnetLattice(Simulation.LatticeParameters parameters) {
        this.parameters = parameters;
    }

    public Magnet[][] getMagnets() {
        return magnets;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.calculatePartialTotalEnergy();
    }

    // todo: private
    public void buildMagnets() {
        int[][] lattice = parameters.lattice();
        Magnet[][] magnets = new Magnet[lattice.length][lattice[0].length];


        for (int i = 0; i < lattice.length; i++) {
            for (int j = 0; j < lattice[0].length; j++) {
                int magnetState = lattice[i][j];
                Magnet newMagnet = new Magnet(magnetState, states, this.externalFieldAngle, externalParams);
                magnets[i][j] = newMagnet;
                newMagnet.addObserver(this);
            }
        }

        this.neighborCalculation = new Level1NeighborCalculation(magnets.length, magnets[0].length);

        for (int i = 0; i < magnets.length; i++) {
            for (int j = 0; j < magnets[0].length; j++) {
                Map<Integer, List<Point>> neighbors = this.neighborCalculation.addNeighbors(new HashMap<>(), i, j);

                Map<Integer, List<Magnet>> mappedNeighbors = neighbors
                        .entrySet()
                        .stream()
                        .collect(
                                Collectors.toMap(x -> x.getKey(),
                                        entry ->  entry.getValue().stream().map( point -> magnets[point.getX()][point.getY()]).collect(Collectors.toList())
                                ));

                magnets[i][j].setNeighbors(mappedNeighbors);
                magnets[i][j].calculatePartialTotalEnergy();
            }
        }
        this.magnets = magnets;
    }

    public void setMagnets(Magnet[][] magnets) {
        this.magnets = magnets;
    }

    public int[][] buildLattice() {
        int[][] lattice = new int[magnets.length][magnets[0].length];

        for (int i = 0; i < magnets.length; i++) {
            for (int j = 0; j < magnets[0].length; j++) {
                int magnetState = magnets[i][j].getState();
                lattice[i][j] = magnetState;
            }
        }

        return lattice;
    }

    public void setStates(int states) {
        this.states = states;
    }

    public void setExternalFieldAngle(double externalFieldAngle) {
        this.externalFieldAngle = externalFieldAngle;
    }

    public void setExternalParams(List<Double> externalParams) {
        this.externalParams = externalParams;
    }
}
