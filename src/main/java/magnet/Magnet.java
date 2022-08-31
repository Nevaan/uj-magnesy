package magnet;

import point.Point;

import java.util.List;
import java.util.Map;

public class Magnet {

    private int state;
    private Map<Integer, List<Point>> neighbors;

    public Magnet(int state, Map<Integer, List<Point>> neighbors) {
        this.state = state;
        this.neighbors = neighbors;
    }

    public int getState() {
        return state;
    }

    public Map<Integer, List<Point>> getNeighbors() {
        return neighbors;
    }

    public void changeState(int change) {
        this.state += change;
    }

}
