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

    public MCSimulation() {
        this.latticeParameters = new LatticeParametersImpl();

    }

    @Override
    public void setLattice(int[][] lattice, int states) {
        this.latticeParameters.setLattice(deepCopyLattice(lattice));
        this.states = states;
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

        E = countTotalEnergy(this.latticeParameters.lattice());

        this.latticeParameters.setTotalEnergy(E);
        this.latticeParameters.setOrderParameter(countOrderParameter(this.latticeParameters.lattice()));
        this.latticeParameters.setNearestNeighbourOrder(countNearestNeighbourOrder(this.latticeParameters.lattice()));

        for (int i = 0; i < steps; i++) {
            singleStep();
        }

        System.out.println("E: " + E);
        double afterSim = countTotalEnergy(this.latticeParameters.lattice());
        System.out.println("AfterSim E: " + afterSim);
        System.out.println("Equal? " + (E == afterSim));
    }

    @Override
    public LatticeParameters getState() {
        return latticeParameters;
    }


    public void singleStep() {

        changeAttempts += 1;

        int[][] currentState = deepCopyLattice(this.latticeParameters.lattice());
        int maxX = currentState.length;
        int maxY = currentState[0].length;

        int randomX = randomInt(maxX);
        int randomY = randomInt(maxY);

        int currentValue = currentState[randomX][randomY];

        if ((acceptedChanges / changeAttempts) > 0.5) {
            numberOfMagnetsToChange++;
        } else if ((acceptedChanges / changeAttempts) < 0.4 && numberOfMagnetsToChange > 1) {
            numberOfMagnetsToChange--;
        }

        for (int i = 0; i < numberOfMagnetsToChange; i++) {
            int change = 0;
            if(currentValue == 0) {
                change = 1;
            } else if (currentValue == states - 1) {
                change = -1;
            } else {
                change = randomInt(2) == 0 ? -1 : 1;
            }

            currentState[randomX][randomY] += change;
        }

        double deltaE = countTotalEnergy(currentState) - countTotalEnergy(this.latticeParameters.lattice());
        //double deltaE = countEi(currentState, randomX, randomY) - countEi(this.latticeParameters.lattice(), randomX, randomY);
        double P = this.probabilityAlgorithm.getProbability(deltaE, TkB);
        double R = new Random().nextDouble();

        if (R < P) {
            acceptedChanges += 1;
            this.latticeParameters.setLattice(deepCopyLattice(currentState));
            this.latticeParameters.setTotalEnergy(countTotalEnergy(currentState));
            this.latticeParameters.setOrderParameter(countOrderParameter(currentState));
            this.latticeParameters.setNearestNeighbourOrder(countNearestNeighbourOrder(currentState));
            E += deltaE;
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

        return result;
    }

    private int randomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    //todo: private method
    public double countAngle(int angleAsInteger) {
        return 2 * Math.PI * angleAsInteger / states;
    }

    public double countTotalEnergy(int[][] lattice) {

        double Etot = 0.0;

        for (int x = 0; x < lattice.length; x++) {

            for (int y = 0; y < lattice[x].length; y++) {
                Etot += countEi(lattice, x, y);
            }
        }

        Etot *= 0.5;

        for (int x = 0; x < lattice.length; x++) {

            for (int y = 0; y < lattice[x].length; y++) {
                Etot -= parameters.get(0) * Math.cos(countAngle(lattice[x][y]) - this.externalFieldAngle);
            }
        }

        return Etot;
    }

    public double countEi(int[][] lattice, int x, int y) {
        double Ei = 0;

        int magnet = lattice[x][y];

        Map<Integer, List<Point>> neighbours = makeNeighborsCalculation(x,y,lattice);
        List<Double> neighborParams = parameters.subList(1, parameters.size());
        ListIterator<Double> iterator = neighborParams.listIterator();

        while (iterator.hasNext()) {
            int idx = iterator.nextIndex();
            Double Cn = iterator.next();
            List<Point> nLevelNeighbors = neighbours.get(idx + 1);

            for(Point p : nLevelNeighbors) {
                int nLevelNeighbor = lattice[p.getX()][p.getY()];
                Ei -= Cn * Math.cos(countAngle(magnet) - countAngle(nLevelNeighbor));
            }
        }

        return Ei;
    }


    // todo: private
    public Map<Integer, List<Point>> makeNeighborsCalculation(int x, int y, int[][] lattice) {

        Map result = new HashMap();

        List<Point> levelOneCoordinates = new ArrayList<>();
        levelOneCoordinates.add(new Point(checkX(x, lattice.length), checkY(y-1, lattice[0].length)));
        levelOneCoordinates.add(new Point(checkX(x, lattice.length), checkY(y+1, lattice[0].length)));
        levelOneCoordinates.add(new Point(checkX(x+1, lattice.length), checkY(y, lattice[0].length)));
        levelOneCoordinates.add(new Point(checkX(x-1, lattice.length), checkY(y, lattice[0].length)));

        result.put(1, levelOneCoordinates);


        List<Point> levelTwoCoordinates = new ArrayList<>();


        levelTwoCoordinates.add(new Point(checkX(x-1, lattice.length), checkY(y+1, lattice[0].length)));
        levelTwoCoordinates.add(new Point(checkX(x+1, lattice.length), checkY(y+1, lattice[0].length)));
        levelTwoCoordinates.add(new Point(checkX(x-1, lattice.length), checkY(y-1, lattice[0].length)));
        levelTwoCoordinates.add(new Point(checkX(x+1, lattice.length), checkY(y-1, lattice[0].length)));

        result.put(2, levelTwoCoordinates);

        List<Point> levelThreeCoordinates = new ArrayList<>();


        levelThreeCoordinates.add(new Point(checkX(x, lattice.length), checkY(y+2, lattice[0].length)));
        levelThreeCoordinates.add(new Point(checkX(x, lattice.length), checkY(y-2, lattice[0].length)));
        levelThreeCoordinates.add(new Point(checkX(x-2, lattice.length), checkY(y, lattice[0].length)));
        levelThreeCoordinates.add(new Point(checkX(x+2, lattice.length), checkY(y, lattice[0].length)));

        result.put(3, levelThreeCoordinates);

        List<Point> levelFourCoordinates = new ArrayList<>();


        levelFourCoordinates.add(new Point(checkX(x-1, lattice.length), checkY(y+2, lattice[0].length)));
        levelFourCoordinates.add(new Point(checkX(x+1, lattice.length), checkY(y+2, lattice[0].length)));
        levelFourCoordinates.add(new Point(checkX(x-2, lattice.length), checkY(y+1, lattice[0].length)));
        levelFourCoordinates.add(new Point(checkX(x+2, lattice.length), checkY(y+1, lattice[0].length)));
        levelFourCoordinates.add(new Point(checkX(x-2, lattice.length), checkY(y-1, lattice[0].length)));
        levelFourCoordinates.add(new Point(checkX(x+2, lattice.length), checkY(y-1, lattice[0].length)));
        levelFourCoordinates.add(new Point(checkX(x-1, lattice.length), checkY(y-2, lattice[0].length)));
        levelFourCoordinates.add(new Point(checkX(x+1, lattice.length), checkY(y-2, lattice[0].length)));

        result.put(4, levelFourCoordinates);


        List<Point> levelFiveCoordinates = new ArrayList<>();

        levelFiveCoordinates.add(new Point(checkX(x-2, lattice.length), checkY(y+2, lattice[0].length)));
        levelFiveCoordinates.add(new Point(checkX(x+2, lattice.length), checkY(y+2, lattice[0].length)));
        levelFiveCoordinates.add(new Point(checkX(x-2, lattice.length), checkY(y-2, lattice[0].length)));
        levelFiveCoordinates.add(new Point(checkX(x+2, lattice.length), checkY(y-2, lattice[0].length)));

        result.put(5, levelFiveCoordinates);

        return result;
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

    public double countOrderParameter(int[][] lattice) {
        int maxX = lattice.length;
        int maxY = lattice[0].length;

        int N = maxX * maxY;

        double xAvg = 0.0;
        double yAvg = 0.0;

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                double angle = countAngle(lattice[x][y]);
                xAvg += Math.cos(angle);
                yAvg += Math.sin(angle);
            }
        }

        xAvg = xAvg / N;
        yAvg = yAvg / N;


        return Math.sqrt(Math.pow(xAvg, 2) + Math.pow(yAvg, 2));
    }

    public double countNearestNeighbourOrder(int[][] lattice) {

        int maxX = lattice.length;
        int maxY = lattice[0].length;

        int N = maxX * maxY;

        int denominator = N * 4;
        double order = 0.0;

        for (int x = 0; x < maxX; x++) {
            int[] innerLattice = lattice[x];

            for (int y = 0; y < innerLattice.length; y++) {
                Map<Integer, List<Point>> neighbors =  makeNeighborsCalculation(x,y, lattice);
                List<Point> levelOneNeighbors = neighbors.get(1);
                for (Point p: levelOneNeighbors) {
                    int magnet = lattice[p.getX()][p.getY()];

                    order += Math.cos(countAngle(lattice[x][y]) - countAngle(magnet));
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
