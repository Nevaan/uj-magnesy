package processor;

public class ParameterResult {

    private final double totalEnergy;
    private final double order;
    private final double nearestNeighborOrder;

    public ParameterResult(double totalEnergy, double order, double nearestNeighborOrder) {
        this.totalEnergy = totalEnergy;
        this.order = order;
        this.nearestNeighborOrder = nearestNeighborOrder;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }

    public double getOrder() {
        return order;
    }

    public double getNearestNeighborOrder() {
        return nearestNeighborOrder;
    }
}
