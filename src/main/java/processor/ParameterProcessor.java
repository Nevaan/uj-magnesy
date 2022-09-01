package processor;

import magnet.Magnet;
import util.Tuple;
import visitor.AbstractVisitor;
import visitor.EnergyVisitor;
import visitor.NearestNeighborVisitor;
import visitor.OrderParameterVisitor;

import java.util.List;

public class ParameterProcessor {

    private AbstractVisitor<Double> energyVisitor;
    private AbstractVisitor<Tuple<Double, Double>> orderParameterVisitor;
    private AbstractVisitor<Double> nearestNeighborVisitor;

    public ParameterProcessor(int states, List<Double> parameters, double externalFieldAngle) {
        energyVisitor = new EnergyVisitor(states, parameters, externalFieldAngle);
        orderParameterVisitor = new OrderParameterVisitor(states);
        nearestNeighborVisitor = new NearestNeighborVisitor(states);
    }

    public ParameterResult process(Magnet[][] magnets) {

        int rows = magnets.length;
        int columns = magnets[0].length;

        double Etot = 0.0;

        double nearestNeighborOrder = 0.0;

        double xAvg = 0.0;
        double yAvg = 0.0;

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {

                Magnet magnet = magnets[x][y];

                Etot += energyVisitor.visit(magnet);

                Tuple<Double, Double> orderPartialResult = orderParameterVisitor.visit(magnet);
                xAvg += orderPartialResult.getLeft();
                yAvg += orderPartialResult.getRight();

                nearestNeighborOrder += nearestNeighborVisitor.visit(magnet);

            }
        }

        xAvg = xAvg / (rows * columns);
        yAvg = yAvg / (rows * columns);

        double orderParameter = Math.sqrt(Math.pow(xAvg, 2) + Math.pow(yAvg, 2));
        nearestNeighborOrder = nearestNeighborOrder / (4 * rows * columns);

        return new ParameterResult(Etot, orderParameter, nearestNeighborOrder);
    }

}
