package magnet;

import util.RandomGenerator;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Magnet {

    private int state;
    private int states;
    private Map<Integer, List<Magnet>> neighbours;
    private MagnetMemento previousState;

    public Magnet(int state, int states) {
        this.state = state;
        this.states= states;
        previousState = new MagnetMemento(this, state);
    }

    public void setNeighbours(Map<Integer, List<Magnet>> neighbours) {
        this.neighbours = neighbours;
    }

    public int getState() {
        return state;
    }

    public Map<Integer, List<Magnet>> getNeighbours() {
        return neighbours;
    }

    public void changeState() {
        previousState = new MagnetMemento(this, state);

        int change = 0;
        if (state == 0) {
            change = 1;
        } else if (state == states - 1) {
            change = -1;
        } else {
            change = RandomGenerator.getInt(2) == 0 ? -1 : 1;
        }

        this.state += change;
    }

    public void restoreState() {
        this.previousState.restore();
    }

    private class MagnetMemento {
        private final Magnet magnet;
        private final int state;

        private MagnetMemento(Magnet magnet, int state) {
            this.magnet = magnet;
            this.state = state;
        }

        private void restore() {
            this.magnet.state = state;
        }
    }


}
