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

        this.magnetLattice = new MagnetLattice(this.latticeParameters.lattice());

        E = countTotalEnergy(this.magnetLattice.getMagnets());

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

        this.magnetLattice.saveState();

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
            int currentValue = currentState[changePoint.getX()][changePoint.getY()].getState();
            int change = 0;
            if(currentValue == 0) {
                change = 1;
            } else if (currentValue == states - 1) {
                change = -1;
            } else {
                change = randomInt(2) == 0 ? -1 : 1;
            }

            currentState[changePoint.getX()][changePoint.getY()].changeState(change);
        }

        double deltaE = countTotalEnergy(currentState) - countTotalEnergy(this.magnetLattice.getPreviousState());

        double P = this.probabilityAlgorithm.getProbability(deltaE, TkB);
        double R = new Random().nextDouble();

        if (R < P) {
            acceptedChanges += 1;
            this.latticeParameters.setLattice(this.magnetLattice.asLattice());
            this.latticeParameters.setTotalEnergy(countTotalEnergy(currentState));
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

    public double countTotalEnergy(Magnet[][] magnetLattice) {

        double Etot = 0.0;

        for (int x = 0; x < magnetLattice.length; x++) {

            for (int y = 0; y < magnetLattice[x].length; y++) {
                Etot += countEi(magnetLattice, x, y);
            }
        }

        Etot *= 0.5;

        for (int x = 0; x < magnetLattice.length; x++) {

            for (int y = 0; y < magnetLattice[x].length; y++) {
                Etot -= parameters.get(0) * Math.cos(countAngle(magnetLattice[x][y].getState()) - this.externalFieldAngle);
            }
        }

        return Etot;
    }

    public double countEi(Magnet[][] magnetLattice, int x, int y) {
        double Ei = 0;

        Magnet magnet = magnetLattice[x][y];

        Map<Integer, List<Point>> neighbours = magnet.getNeighbors();
        List<Double> neighborParams = parameters.subList(1, parameters.size());
        ListIterator<Double> iterator = neighborParams.listIterator();

        while (iterator.hasNext()) {
            int idx = iterator.nextIndex();
            Double Cn = iterator.next();
            List<Point> nLevelNeighbors = neighbours.get(idx + 1);

            for(Point p : nLevelNeighbors) {
                Magnet nLevelNeighbor = magnetLattice[p.getX()][p.getY()];
                Ei -= Cn * Math.cos(countAngle(magnet.getState()) - countAngle(nLevelNeighbor.getState()));
            }
        }

        return Ei;
    }

    private int checkX(int x, int maxX) {
        if(x<0) {
            return maxX + x;
        }

        if(x>=maxX) {
            return x - maxX;
        }

        return x;
    }

    private int checkY(int y, int maxY) {

        if(y<0) {
            return maxY + y;
        }

        if(y>=maxY) {
            return y - maxY;
        }

        return y;
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
                Map<Integer, List<Point>> neighbors = innerLattice[y].getNeighbors();
                List<Point> levelOneNeighbors = neighbors.get(1);
                for (Point p: levelOneNeighbors) {
                    int magnet = lattice[p.getX()][p.getY()].getState();

                    order += Math.cos(countAngle(lattice[x][y].getState()) - countAngle(magnet));
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
