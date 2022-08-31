package magnet;

public class MagnetLatticeMemento {

    private MagnetLattice magnetLattice;
    private Magnet[][] magnets;

    public MagnetLatticeMemento(MagnetLattice magnetLattice) {
        this.magnetLattice = magnetLattice;
        this.magnets = deepCopyArray(magnetLattice.getMagnets());
    }


    public void restore() {
        this.magnetLattice.setMagnets(this.magnets);
    }

    private Magnet[][] deepCopyArray(Magnet[][] originalMagnets) {
        Magnet[][] copy = new Magnet[originalMagnets.length][originalMagnets[0].length];

        for (int x = 0; x < originalMagnets.length; x++) {
            for (int y = 0; y < originalMagnets[x].length; y++) {

                copy[x][y] = new Magnet(originalMagnets[x][y].getState());

            }
        }

        return copy;
    }

    // todo : refactor - should not be exposed
    public Magnet[][] getMagnets() {
        return magnets;
    }
}
