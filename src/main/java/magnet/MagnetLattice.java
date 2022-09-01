package magnet;

import neighbor.Level1NeighborCalculation;
import neighbor.NeighborCalculation;
import point.Point;

import java.util.*;
import java.util.stream.Collectors;

public class MagnetLattice {

    private List<Double> parameters;
    private int states;
    private double externalFieldAngle;

    private Magnet[][] magnets;
    private MagnetLatticeMemento lastState;
    private NeighborCalculation neighborCalculation;
    private double totalEnergy;
    private double orderParamter;
    private double nearestNeighborOrder;


    public MagnetLattice(int[][] lattice, List<Double> parameters, double externalFieldAngle, int states) {

        this.parameters = parameters;
        this.externalFieldAngle = externalFieldAngle;
        this.states = states;

        this.neighborCalculation = new Level1NeighborCalculation(lattice.length, lattice[0].length);

        Magnet[][] magnets = new Magnet[lattice.length][lattice[0].length];

        for (int x = 0; x < lattice.length; x++) {
            for (int y = 0; y < lattice[x].length; y++) {

                magnets[x][y] = new Magnet(lattice[x][y]);

            }
        }

        for (int x = 0; x < lattice.length; x++) {
            for (int y = 0; y < lattice[x].length; y++) {


            Map<Integer, List<Point>> neighbors = neighborCalculation.addNeighbors(new HashMap<>(), x, y);

            Map<Integer, List<Magnet>> neighborsAsMagnets =  neighbors.entrySet().stream()
                    .collect(
                            Collectors.toMap(
                                    a-> a.getKey(),
                                    b -> b.getValue().stream().map(point -> magnets[point.getX()][point.getY()]).collect(Collectors.toList())
                            )
                    );

                magnets[x][y].setNeighbors(neighborsAsMagnets);
            }
        }


        this.magnets = magnets;
        singleLoop();

    }

    public void setMagnets(Magnet[][] magnets) {
        this.magnets = magnets;
    }

    public Magnet[][] getMagnets() {
        return magnets;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }

    public double getOrderParamter() {
        return orderParamter;
    }

    public double getNearestNeighborOrder() {
        return nearestNeighborOrder;
    }

    public int[][] asLattice() {
        int[][] lattice = new int[magnets.length][magnets[0].length];

        for (int x = 0; x < magnets.length; x++) {
            for (int y = 0; y < magnets[x].length; y++) {

                lattice[x][y] = magnets[x][y].getState();

            }
        }

        return lattice;
    }

    public void undoChanges() {
        this.lastState.restore();
    }

    public double applyChanges(Set<Point> pointsToChange) {

        Set<Magnet> magnetsToChange = pointsToChange.stream().map(point -> magnets[point.getX()][point.getY()]).collect(Collectors.toSet());
        this.lastState = new MagnetLatticeMemento(this, magnetsToChange, this.totalEnergy, this.orderParamter, this.nearestNeighborOrder);

        for (Magnet magnet : magnetsToChange) {
            int currentValue = magnet.getState();
            int change = 0;
            if(currentValue == 0) {
                change = 1;
            } else if (currentValue == states - 1) {
                change = -1;
            } else {
                change = randomInt(2) == 0 ? -1 : 1;
            }

            magnet.changeState(change);
        }

        singleLoop();

        return this.totalEnergy - lastState.totalEnergy;
    }

    private int randomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public double countAngle(int angleAsInteger) {
        return 2 * Math.PI * angleAsInteger / states;
    }


    private void singleLoop() {
        double Etot = 0.0;

        int N = magnets.length * magnets[0].length;

        double xAvg = 0.0;
        double yAvg = 0.0;

        int denominator = N * 4;
        double order = 0.0;


        for (int x = 0; x < magnets.length; x++) {
            for (int y = 0; y < magnets[x].length; y++) {
                Etot += singleMagnetEnergy(x,y);

                double angle = countAngle(magnets[x][y].getState());
                xAvg += Math.cos(angle);
                yAvg += Math.sin(angle);

                Map<Integer, List<Magnet>> neighbors = magnets[x][y].getNeighbors();
                List<Magnet> levelOneNeighbors = neighbors.get(1);
                for (Magnet magnet: levelOneNeighbors) {
                    order += Math.cos(countAngle(magnets[x][y].getState()) - countAngle(magnet.getState()));
                }
            }
        }

        xAvg = xAvg / N;
        yAvg = yAvg / N;

        this.totalEnergy = Etot;
        this.orderParamter = Math.sqrt(Math.pow(xAvg, 2) + Math.pow(yAvg, 2));
        this.nearestNeighborOrder = order / denominator;
    }

    private double singleMagnetEnergy(int x, int y) {
        double Etot = 0.0;
        Etot += 0.5 * countEi(x, y);
        Etot -= parameters.get(0) * Math.cos(countAngle(magnets[x][y].getState()) - this.externalFieldAngle);
        return Etot;
    }

    public double countEi(int x, int y) {
        double Ei = 0;

        Magnet magnet = magnets[x][y];

        Map<Integer, List<Magnet>> neighbours = magnet.getNeighbors();
        List<Double> neighborParams = parameters.subList(1, parameters.size());
        ListIterator<Double> iterator = neighborParams.listIterator();

        while (iterator.hasNext()) {
            int idx = iterator.nextIndex();
            Double Cn = iterator.next();
            List<Magnet> nLevelNeighbors = neighbours.get(idx + 1);

            for(Magnet neighbor : nLevelNeighbors) {
                Ei -= Cn * Math.cos(countAngle(magnet.getState()) - countAngle(neighbor.getState()));
            }
        }

        return Ei;
    }


    private class MagnetLatticeMemento {

        private MagnetLattice magnetLattice;
        private final double totalEnergy;
        private final double orderParamter;
        private final double nearestNeighborOrder;

        private Set<Magnet> magnets;

        public MagnetLatticeMemento(MagnetLattice magnetLattice, Set<Magnet> magnetsToChange, double totalEnergy, double orderParamter, double nearestNeighborOrder) {
            this.magnetLattice = magnetLattice;
            this.magnets = magnetsToChange;
            this.totalEnergy = totalEnergy;
            this.orderParamter = orderParamter;
            this.nearestNeighborOrder = nearestNeighborOrder;
        }


        public void restore() {
            this.magnets.stream().forEach(Magnet::restoreState);
            this.magnetLattice.totalEnergy = totalEnergy;
            this.magnetLattice.orderParamter = orderParamter;
            this.magnetLattice.nearestNeighborOrder = nearestNeighborOrder;
        }

    }

}
