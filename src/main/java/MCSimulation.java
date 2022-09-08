import magnet.MagnetLattice;
import main.Simulation;
import point.Point;
import probability.Glauber;
import probability.Metropolis;
import probability.ProbabilityAlgorithm;
import processor.ParameterProcessor;
import util.RandomGenerator;
import util.Tuple;

import java.util.*;
import java.util.concurrent.Executors;

public class MCSimulation implements Simulation {

    private ProbabilityAlgorithm probabilityAlgorithm;
    private double temperatureBoltzmannConstant;

    private final ParameterProcessor.Builder parameterProcessorBuilder;
    private final MagnetLattice.Builder magnetLatticeBuilder;
    private MagnetLattice magnetLattice;

    private int numberOfMagnetsToChange = 1;
    private double acceptedChanges = 0;
    private double changeAttempts = 0;

    public MCSimulation() {
        this.parameterProcessorBuilder = new ParameterProcessor.Builder(Executors.newCachedThreadPool());
        this.magnetLatticeBuilder = new MagnetLattice.Builder();
    }

    @Override
    public void setLattice(int[][] lattice, int states) {
        this.parameterProcessorBuilder.setStates(states);
        this.magnetLatticeBuilder.setStates(states);
        this.magnetLatticeBuilder.setLattice(lattice);
    }

    @Override
    public void setEnergyParameters(List<Double> parameters, double externalFieldAngle) {
        this.parameterProcessorBuilder.setParameters(parameters);
        this.parameterProcessorBuilder.setExternalFieldAngle(externalFieldAngle);
    }

    @Override
    public void setProbabilityFormula(ProbabilityFormula formula) {
        switch (formula) {
            case GLAUBER:
                this.probabilityAlgorithm = new Glauber();
                break;
            case METROPOLIS:
                this.probabilityAlgorithm = new Metropolis();
                break;
        }
    }

    @Override
    public void setTkB(double TkB) {
        this.temperatureBoltzmannConstant = TkB;
    }

    @Override
    public void executeMCSteps(int steps) {
        this.magnetLatticeBuilder.setParameterProcessor(this.parameterProcessorBuilder.build());
        this.magnetLattice = this.magnetLatticeBuilder.build();

        for (int i = 0; i < steps; i++) {
            singleStep();
        }
        this.magnetLatticeBuilder.setLattice(this.magnetLattice.getLatticeParameters().lattice());

    }

    @Override
    public LatticeParameters getState() {
        return this.magnetLattice.getLatticeParameters();
    }

    private void singleStep() {

        changeAttempts += 1;

        Tuple<Integer, Integer> magnetLatticeShape = this.magnetLattice.getShape();

        int maxX = magnetLatticeShape.getLeft();
        int maxY = magnetLatticeShape.getRight();

        double ratio = (acceptedChanges / changeAttempts);


        if (ratio > 0.5 && numberOfMagnetsToChange < (maxX * maxY)) {
            numberOfMagnetsToChange++;
        } else if (ratio < 0.4 && numberOfMagnetsToChange > 1) {
            numberOfMagnetsToChange--;
        }

        Set<Point> pointsToChange = new HashSet<>();

        while (pointsToChange.size() != numberOfMagnetsToChange) {
            int randomX = RandomGenerator.getInt(maxX);
            int randomY = RandomGenerator.getInt(maxY);
            pointsToChange.add(new Point(randomX, randomY));
        }

        double deltaE = this.magnetLattice.applyChanges(pointsToChange);
        double acceptProbability = this.probabilityAlgorithm.getProbability(deltaE, temperatureBoltzmannConstant);

        if (RandomGenerator.getDouble() < acceptProbability) {
            acceptedChanges += 1;
        } else {
            this.magnetLattice.undoChanges();
        }

    }

    public double acceptRatio() {
        return (acceptedChanges / changeAttempts);
    }

    public int magnetsChange() {
        return numberOfMagnetsToChange;
    }

}
