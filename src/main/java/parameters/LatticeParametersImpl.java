package parameters;

import main.Simulation;

public class LatticeParametersImpl implements Simulation.LatticeParameters {

    private int[][] lattice;
    private double totalEnergy;
    private double orderParameter;
    private double nearestNeighbourOrder;

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


    public void setLattice(int[][] lattice) {
        this.lattice = lattice;
    }
    public void setTotalEnergy(double totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public void setOrderParameter(double orderParameter) {
        this.orderParameter = orderParameter;
    }

    public void setNearestNeighbourOrder(double nearestNeighbourOrder) {
        this.nearestNeighbourOrder = nearestNeighbourOrder;
    }
}
