package magnet;

import java.util.*;

// TODO: dodac pamiatke zapisujaca stan przed zmiana + restore jak zmiana jest odrzucona
public class Magnet extends Observable {

    private int state;
    private int numberOfStates;
    private Map<Integer, List<Magnet>> neighbors;
    private double externalFieldAngle;

    private double partialTotalEnergy;
    private List<Double> params;


    public Magnet(int state, int numberOfStates, double externalFieldAngle, List<Double> params) {
        this.state = state;
        this.numberOfStates = numberOfStates;
        this.externalFieldAngle = externalFieldAngle;
        this.params = params;
    }

    public void changeState() {
        if (state == 0) {
            state += 1;
        } else if (state == (numberOfStates - 1)) {
            state += -1;
        } else {
            state += randomInt(2) == 0 ? -1 : 1;
        }

        setChanged();
        notifyObservers();
    }

    private int randomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public int getState() {
        return state;
    }


    public void setNeighbors(Map<Integer, List<Magnet>> neighbors) {
        this.neighbors = neighbors;
        neighbors.values().stream().flatMap(x -> x.stream()).forEach(n -> n.addObserver(this));
    }

    public Map<Integer, List<Magnet>> getNeighbors() {
        return neighbors;
    }

    public double getPartialTotalEnergy() {
        return partialTotalEnergy;
    }



    public void calculatePartialTotalEnergy() {
        this.partialTotalEnergy = (0.5 * countPartialEi()) - (params.get(0) * Math.cos(countAngle() - externalFieldAngle));
    }

    private double countPartialEi() {
        double Ei = 0;

        List<Double> neighborParams = params.subList(1, params.size());
        ListIterator<Double> iterator = neighborParams.listIterator();

        while (iterator.hasNext()) {
            int idx = iterator.nextIndex();
            Double Cn = iterator.next();
            List<Magnet> nLevelNeighbors = getNeighbors().get(idx + 1);

            for(Magnet p : nLevelNeighbors) {
                Ei -= Cn * Math.cos(countAngle() - p.countAngle());
            }
        }

        return Ei;
    }


    public double countAngle() {
        return 2 * Math.PI * state / numberOfStates;
    }
}
