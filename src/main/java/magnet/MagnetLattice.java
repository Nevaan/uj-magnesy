package magnet;

import neighbor.Level1NeighborCalculation;
import neighbor.NeighborCalculation;
import point.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagnetLattice {

    private Magnet[][] magnets;
    private MagnetLatticeMemento lastState;
    private NeighborCalculation neighborCalculation;

    public MagnetLattice(int[][] lattice) {

        this.neighborCalculation = new Level1NeighborCalculation(lattice.length, lattice[0].length);

        Magnet[][] magnets = new Magnet[lattice.length][lattice[0].length];

        for (int x = 0; x < lattice.length; x++) {
            for (int y = 0; y < lattice[x].length; y++) {

                Map<Integer, List<Point>> neighbors = neighborCalculation.addNeighbors(new HashMap<>(), x, y);

                magnets[x][y] = new Magnet(lattice[x][y], neighbors);

            }
        }

        this.magnets = magnets;


    }

    public void setMagnets(Magnet[][] magnets) {
        this.magnets = magnets;
    }

    public Magnet[][] getMagnets() {
        return magnets;
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

    public void saveState() {
        this.lastState = new MagnetLatticeMemento(this);
    }

    public void undoChanges() {
        this.lastState.restore();
    }

    public Magnet[][] getPreviousState() {
        return this.lastState.getMagnets();
    }

}
