import magnet.Magnet;
import magnet.MagnetLattice;
import main.Simulation;
import parameters.LatticeParametersImpl;
import point.Point;
import probability.Glauber;
import probability.Metropolis;
import probability.ProbabilityAlgorithm;

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
        this.latticeParameters.setLattice(deepCopyLattice(lattice));
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

    double E;

    @Override
    public void executeMCSteps(int steps) {

        this.magnetLattice = new MagnetLattice(this.latticeParameters.lattice(), this.parameters, this.externalFieldAngle, this.states);

        E = this.magnetLattice.getTotalEnergy();

        this.latticeParameters.setTotalEnergy(E);
        this.latticeParameters.setOrderParameter(countOrderParameter(this.magnetLattice.getMagnets()));
        this.latticeParameters.setNearestNeighbourOrder(countNearestNeighbourOrder(this.magnetLattice.getMagnets()));

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
            this.latticeParameters.setOrderParameter(countOrderParameter(currentState));
            this.latticeParameters.setNearestNeighbourOrder(countNearestNeighbourOrder(currentState));
            E += deltaE;
        } else {
            this.magnetLattice.undoChanges();
        }


    }

    // todo: remove
    private void printLattice(int[][] x) {
//        for (int i = 0; i < x.length; i++) {
//            System.out.print("\t\t");
//            for (int j = 0; j < x[i].length; j++) {
//                System.out.print(x[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
    }

    private int[][] deepCopyLattice(int[][] original) {
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }

        latticeCheck(result);

        return result;
    }


    //todo: remove
    private void latticeCheck(int[][] lattice) {
        for (int x = 0; x < lattice.length; x++) {

            for (int y = 0; y < lattice[x].length; y++) {
                if (lattice[x][y] < 0 || lattice[x][y] > states - 1) {
                    throw new RuntimeException("not in range: " + lattice[x][y]);
                }
            }
        }

    }

    private int randomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    //todo: private method
    public double countAngle(int angleAsInteger) {
        return 2 * Math.PI * angleAsInteger / states;
    }

    public double countOrderParameter(Magnet[][] lattice) {
        int maxX = lattice.length;
        int maxY = lattice[0].length;

        int N = maxX * maxY;

        double xAvg = 0.0;
        double yAvg = 0.0;

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                double angle = countAngle(lattice[x][y].getState());
                xAvg += Math.cos(angle);
                yAvg += Math.sin(angle);
            }
        }

        xAvg = xAvg / N;
        yAvg = yAvg / N;


        return Math.sqrt(Math.pow(xAvg, 2) + Math.pow(yAvg, 2));
    }

    public double countNearestNeighbourOrder(Magnet[][] lattice) {

        int maxX = lattice.length;
        int maxY = lattice[0].length;

        int N = maxX * maxY;

        int denominator = N * 4;
        double order = 0.0;

        for (int x = 0; x < maxX; x++) {
            Magnet[] innerLattice = lattice[x];

            for (int y = 0; y < innerLattice.length; y++) {
                Map<Integer, List<Magnet>> neighbors = innerLattice[y].getNeighbors();
                List<Magnet> levelOneNeighbors = neighbors.get(1);
                for (Magnet magnet: levelOneNeighbors) {
                    order += Math.cos(countAngle(lattice[x][y].getState()) - countAngle(magnet.getState()));
                }
            }
        }
        return order / denominator;

    }


    public void setExternalFieldAngle(double externalFieldAngle) {
        this.externalFieldAngle = externalFieldAngle;
    }

    public void setStates(int states) {
        this.states = states;
    }


}
