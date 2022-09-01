import magnet.Magnet;
import magnet.MagnetLattice;
import main.Simulation;
import parameters.LatticeParametersImpl;
import point.Point;
import probability.Glauber;
import probability.Metropolis;
import probability.ProbabilityAlgorithm;
import processor.ParameterProcessor;

import java.util.*;

public class MCSimulation implements Simulation {

    private LatticeParametersImpl latticeParameters;

    private ProbabilityAlgorithm probabilityAlgorithm;
    private List<Double> parameters;
    private double externalFieldAngle;
    private double TkB;
    private int states;

    private int numberOfMagnetsToChange = 1;
    private double acceptedChanges = 0;
    private double changeAttempts = 0;


    private MagnetLattice magnetLattice;

    public MCSimulation() {
        this.latticeParameters = new LatticeParametersImpl();
    }

    @Override
    public void setLattice(int[][] lattice, int states) {
        this.states = states;
        this.latticeParameters.setLattice(lattice);
    }

    @Override
    public void setEnergyParameters(List<Double> parameters, double externaFieldAngle) {
        this.parameters = parameters;
        this.externalFieldAngle = externaFieldAngle;
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
        this.magnetLattice = new MagnetLattice(this.latticeParameters.lattice(), new ParameterProcessor(this.states, this.parameters, this.externalFieldAngle), this.states);

        this.latticeParameters.setTotalEnergy(this.magnetLattice.getTotalEnergy());
        this.latticeParameters.setOrderParameter(this.magnetLattice.getOrderParameter());
        this.latticeParameters.setNearestNeighbourOrder(this.magnetLattice.getNearestNeighborOrder());

        for (int i = 0; i < steps; i++) {
            singleStep();
        }

    }

    @Override
    public LatticeParameters getState() {
        return latticeParameters;
    }

    public void singleStep() {

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
            this.latticeParameters.setLattice(this.magnetLattice.asLattice());
            this.latticeParameters.setTotalEnergy(this.magnetLattice.getTotalEnergy());
            this.latticeParameters.setOrderParameter(this.magnetLattice.getOrderParameter());
            this.latticeParameters.setNearestNeighbourOrder(this.magnetLattice.getNearestNeighborOrder());
        } else {
            this.magnetLattice.undoChanges();
        }


    }

    private int randomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public void setStates(int states) {
        this.states = states;
    }


}
