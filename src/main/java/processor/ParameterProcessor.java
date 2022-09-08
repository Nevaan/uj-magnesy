package processor;

import magnet.Magnet;
import util.Tuple;
import visitor.AbstractVisitor;
import visitor.EnergyVisitor;
import visitor.NearestNeighborVisitor;
import visitor.OrderParameterVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParameterProcessor {

    private final AbstractVisitor<Double> energyVisitor;
    private final AbstractVisitor<Tuple<Double, Double>> orderParameterVisitor;
    private final AbstractVisitor<Double> nearestNeighborVisitor;


    private final ExecutorService executorService;

    private ParameterProcessor(int states, List<Double> parameters, double externalFieldAngle, ExecutorService executorService) {
        energyVisitor = new EnergyVisitor(states, parameters, externalFieldAngle);
        orderParameterVisitor = new OrderParameterVisitor(states);
        nearestNeighborVisitor = new NearestNeighborVisitor(states);
        this.executorService = executorService;
    }

    public ParameterResult process(Magnet[][] magnets) {
        int rows = magnets.length;
        int columns = magnets[0].length;

        double totalEnergy = 0.0;

        double nearestNeighborOrder = 0.0;

        double xAvg = 0.0;
        double yAvg = 0.0;


        List<Future<Triplet>> futures = new ArrayList<>();

        for (int x = 0; x < rows; x++) {

            int xx = x;
            Future<Triplet> future = executorService.submit(() -> {

                double totalEnergyThread = 0.0;
                double xAvgThread = 0.0;
                double yAvgThread = 0.0;
                double nearestNeighborOrderThread = 0.0;

                for (int y = 0; y < columns; y++) {
                    Magnet magnet = magnets[xx][y];

                    totalEnergyThread += energyVisitor.visit(magnet);

                    Tuple<Double, Double> orderPartialResult = orderParameterVisitor.visit(magnet);
                    xAvgThread += orderPartialResult.getLeft();
                    yAvgThread += orderPartialResult.getRight();

                    nearestNeighborOrderThread += nearestNeighborVisitor.visit(magnet);

                }
                return new Triplet(totalEnergyThread, new Tuple<>(xAvgThread, yAvgThread), nearestNeighborOrderThread);
            });

            futures.add(future);



        }

        for (Future<Triplet> tripletFuture: futures) {
            Triplet triplet;
            try {
                triplet =  tripletFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Concurrency related exception", e);
            }
            totalEnergy += triplet.getEnergy();
            xAvg += triplet.orderTuple.getLeft();
            yAvg += triplet.orderTuple.getRight();
            nearestNeighborOrder += triplet.getNeighbourOrder();
        }

        xAvg = xAvg / (rows * columns);
        yAvg = yAvg / (rows * columns);

        double orderParameter = Math.sqrt(Math.pow(xAvg, 2) + Math.pow(yAvg, 2));
        nearestNeighborOrder = nearestNeighborOrder / (4 * rows * columns);

        return new ParameterResult(totalEnergy, orderParameter, nearestNeighborOrder);
    }

    public static class Builder {
        private int states;
        private List<Double> parameters;
        private double externalFieldAngle;
        private final ExecutorService executorSevice;

        public Builder(ExecutorService executorSevice) {
            this.executorSevice = executorSevice;
        }

        public void setStates(int states) {
            this.states = states;
        }

        public void setParameters(List<Double> parameters) {
            this.parameters = parameters;
        }

        public void setExternalFieldAngle(double externalFieldAngle) {
            this.externalFieldAngle = externalFieldAngle;
        }

        public ParameterProcessor build() {
            return new ParameterProcessor(states, parameters, externalFieldAngle, executorSevice);
        }
    }

    private static class Triplet {
        private final double energy;
        private final Tuple<Double, Double> orderTuple;
        private final double neighbourOrder;

        public Triplet(double energy, Tuple<Double, Double> orderTuple, double neighbourOrder) {
            this.energy = energy;
            this.orderTuple = orderTuple;
            this.neighbourOrder = neighbourOrder;
        }

        public double getEnergy() {
            return energy;
        }

        public Tuple<Double, Double> getOrderTuple() {
            return orderTuple;
        }

        public double getNeighbourOrder() {
            return neighbourOrder;
        }
    }

}
