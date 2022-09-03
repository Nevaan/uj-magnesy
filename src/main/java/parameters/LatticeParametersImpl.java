package parameters;

import main.Simulation;

public class LatticeParametersImpl implements Simulation.LatticeParameters {

    private final int[][] lattice;
    private double totalEnergy = 0.0;
    private double orderParameter = 0.0;
    private double nearestNeighbourOrder = 0.0;

    @Override
    public double totalEnergy() {
        return this.totalEnergy;
    }

    @Override
    public double orderParameter() {
        return this.orderParameter;
    }

    @Override
    public double nearestNeighbourOrder() {
        return this.nearestNeighbourOrder;
    }

    @Override
    public int[][] lattice() {
        return lattice;
    }

    public LatticeParametersImpl(int[][] lattice) {
        this.lattice = lattice;
    }

    public LatticeParametersImpl(int[][] lattice, double totalEnergy, double orderParameter, double nearestNeighbourOrder) {
        this.lattice = lattice;
        this.totalEnergy = totalEnergy;
        this.orderParameter = orderParameter;
        this.nearestNeighbourOrder = nearestNeighbourOrder;
    }
}
