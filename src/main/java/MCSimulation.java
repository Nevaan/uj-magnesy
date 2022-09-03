import magnet.Magnet;
import magnet.MagnetLattice;
import main.Simulation;
import point.Point;
import probability.Glauber;
import probability.Metropolis;
import probability.ProbabilityAlgorithm;
import processor.ParameterProcessor;

import java.util.*;

public class MCSimulation implements Simulation {

    private ProbabilityAlgorithm probabilityAlgorithm;
    private double TkB;

    private ParameterProcessor.Builder parameterProcessorBuilder;
    private MagnetLattice.Builder magnetLatticeBuilder;
    private MagnetLattice magnetLattice;

    private int numberOfMagnetsToChange = 1;
    private double acceptedChanges = 0;
    private double changeAttempts = 0;

    public MCSimulation() {
        this.parameterProcessorBuilder = new ParameterProcessor.Builder();
        this.magnetLatticeBuilder = new MagnetLattice.Builder();
    }

    @Override
    public void setLattice(int[][] lattice, int states) {
        this.parameterProcessorBuilder.setStates(states);
        this.magnetLatticeBuilder.setStates(states);
        this.magnetLatticeBuilder.setLattice(lattice);
    }

    @Override
    public void setEnergyParameters(List<Double> parameters, double externaFieldAngle) {
        this.parameterProcessorBuilder.setParameters(parameters);
        this.parameterProcessorBuilder.setExternalFieldAngle(externaFieldAngle);
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
        this.TkB  = TkB;
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

        Magnet[][] currentState = this.magnetLattice.getMagnets();

        int maxX = currentState.length;
        int maxY = currentState[0].length;

        if ((acceptedChanges / changeAttempts) > 0.5 && numberOfMagnetsToChange < (maxX * maxY)) {
            numberOfMagnetsToChange++;
        } else if ((acceptedChanges / changeAttempts) < 0.4 && numberOfMagnetsToChange > 1) {
            numberOfMagnetsToChange--;
        }

        Set<Point> pointsToChange = new HashSet<>();

        while (pointsToChange.size() != numberOfMagnetsToChange) {
            int randomX = randomInt(maxX);
            int randomY = randomInt(maxY);
            pointsToChange.add(new Point(randomX, randomY));
        }

        double deltaE = this.magnetLattice.applyChanges(pointsToChange);

        double P = this.probabilityAlgorithm.getProbability(deltaE, TkB);
        double R = new Random().nextDouble();

        if (R < P) {
            acceptedChanges += 1;
        } else {
            this.magnetLattice.undoChanges();
        }

    }

    private int randomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

}
