import magnet.Magnet;
import magnet.MagnetLattice;
import main.Simulation;
import neighbor.Level1NeighborCalculation;
import neighbor.NeighborCalculation;
import parameters.LatticeParametersImpl;
import point.Point;
import probability.Glauber;
import probability.Metropolis;
import probability.ProbabilityAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

public class MCSimulation implements Simulation {

    private LatticeParametersImpl latticeParameters;

    private ProbabilityAlgorithm probabilityAlgorithm;
    private double TkB;
    private int states;
    private MagnetLattice magnetLattice;

    private int numberOfMagnetsToChange = 1;
    private double acceptedChanges = 0;
    private double changeAttempts = 0;

    public MCSimulation() {
        this.latticeParameters = new LatticeParametersImpl();
        this.magnetLattice = new MagnetLattice(this.latticeParameters);
    }

    @Override
    public void setLattice(int[][] lattice, int states) {
        this.states = states;
        this.magnetLattice.setStates(states);

        this.latticeParameters.setLattice(lattice);
    }

    @Override
    public void setEnergyParameters(List<Double> parameters, double externaFieldAngle) {
        this.magnetLattice.setExternalParams(parameters);
        this.magnetLattice.setExternalFieldAngle(externaFieldAngle);
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

        this.magnetLattice.buildMagnets();


        Magnet[][] magnets = this.magnetLattice.getMagnets();
        E = countTotalEnergy(magnets);

        this.latticeParameters.setTotalEnergy(E);
        this.latticeParameters.setOrderParameter(countOrderParameter(magnets));
        this.latticeParameters.setNearestNeighbourOrder(countNearestNeighbourOrder(magnets));


        for (int i = 0; i < steps; i++) {
            singleStep();
        }

//        System.out.println("E: " + E);
//        Magnet[][] magnets2=  buildMagnets();
//        double afterSim = countTotalEnergy(magnets2);
//        System.out.println("AfterSim E: " + afterSim);
//        System.out.println("Equal? " + (E == afterSim));
    }

    @Override
    public LatticeParameters getState() {
        return latticeParameters;
    }

    public void singleStep() {

        changeAttempts += 1;

        Magnet[][] currentState = this.magnetLattice.getMagnets();
        double energyBeforeChange = countTotalEnergy(currentState);

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

        for (Point changePoint : pointsToChange) {
            currentState[changePoint.getX()][changePoint.getY()].changeState();
        }

        double deltaE = countTotalEnergy(currentState) - energyBeforeChange;


        double P = this.probabilityAlgorithm.getProbability(deltaE, TkB);
        double R = new Random().nextDouble();

        if (R < P) {
            acceptedChanges += 1;
//            this.latticeParameters.setLattice(buildLattice(currentState));
            this.latticeParameters.setTotalEnergy(countTotalEnergy(currentState));
            this.latticeParameters.setOrderParameter(countOrderParameter(currentState));
            this.latticeParameters.setNearestNeighbourOrder(countNearestNeighbourOrder(currentState));
            E += deltaE;
        }
        // else odrzuc zmiane (restore na magnesach)


    }

    // todo: remove
//    private void printLattice(int[][] x) {
//        for (int i = 0; i < x.length; i++) {
//            System.out.print("\t\t");
//            for (int j = 0; j < x[i].length; j++) {
//                System.out.print(x[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
//
//    private int[][] deepCopyLattice(int[][] original) {
//        int[][] result = new int[original.length][];
//        for (int i = 0; i < original.length; i++) {
//            result[i] = Arrays.copyOf(original[i], original[i].length);
//        }
//
//        latticeCheck(result);
//
//        return result;
//    }


//    //todo: remove
//    private void latticeCheck(int[][] lattice) {
//        for (int x = 0; x < lattice.length; x++) {
//
//            for (int y = 0; y < lattice[x].length; y++) {
//                if (lattice[x][y] < 0 || lattice[x][y] > states - 1) {
//                    throw new RuntimeException("not in range: " + lattice[x][y]);
//                }
//            }
//        }
//
//    }

    //todo:remove
//    public void setNeighborCalculation() {
//        this.neighborCalculation = new Level1NeighborCalculation(this.latticeParameters.lattice().length, this.latticeParameters.lattice()[0].length);
//    }

    private int randomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    //todo: private method
    public double countAngle(Magnet magnet) {
        return 2 * Math.PI * magnet.getState() / states;
    }

    public double countTotalEnergy(Magnet[][] magnets) {

        double Etot = 0.0;

        for (int x = 0; x < magnets.length; x++) {
            for (int y = 0; y < magnets[x].length; y++) {
                Etot += magnets[x][y].getPartialTotalEnergy();
            }
        }

        return Etot;
    }

    public double countOrderParameter(Magnet[][] magnets) {
        int maxX = magnets.length;
        int maxY = magnets[0].length;

        int N = maxX * maxY;

        double xAvg = 0.0;
        double yAvg = 0.0;

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                double angle = countAngle(magnets[x][y]);
                xAvg += Math.cos(angle);
                yAvg += Math.sin(angle);
            }
        }

        xAvg = xAvg / N;
        yAvg = yAvg / N;


        return Math.sqrt(Math.pow(xAvg, 2) + Math.pow(yAvg, 2));
    }

    public double countNearestNeighbourOrder(Magnet[][] magnets) {

        int maxX = magnets.length;
        int maxY = magnets[0].length;

        int N = maxX * maxY;

        int denominator = N * 4;
        double order = 0.0;

        for (int x = 0; x < maxX; x++) {
            Magnet[] magnetRow = magnets[x];

            for (int y = 0; y < magnetRow.length; y++) {
                Map<Integer, List<Magnet>> neighbors =  magnets[x][y].getNeighbors();
                List<Magnet> levelOneNeighbors = neighbors.get(1);
                for (Magnet p: levelOneNeighbors) {
                    order += Math.cos(countAngle(magnets[x][y]) - countAngle(p));
                }
            }
        }
        return order / denominator;

    }

    public void setStates(int states) {
        this.states = states;
    }


}
