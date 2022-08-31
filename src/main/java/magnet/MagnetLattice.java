package magnet;

public class MagnetLattice {

    private Magnet[][] magnets;
    private MagnetLatticeMemento lastState;

    public MagnetLattice(int[][] lattice) {

        Magnet[][] magnets = new Magnet[lattice.length][lattice[0].length];

        for (int x = 0; x < lattice.length; x++) {
            for (int y = 0; y < lattice[x].length; y++) {

                magnets[x][y] = new Magnet(lattice[x][y]);

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
