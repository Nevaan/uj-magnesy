package magnet;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Magnet {

    private int state;
    private int states;
    private Map<Integer, List<Magnet>> neighbors;
    private MagnetMemento previousState;

    public Magnet(int state, int states) {
        this.state = state;
        this.states= states;
        previousState = new MagnetMemento(this, state);
    }

    public void setNeighbors(Map<Integer, List<Magnet>> neighbors) {
        this.neighbors = neighbors;
    }

    public int getState() {
        return state;
    }

    public Map<Integer, List<Magnet>> getNeighbors() {
        return neighbors;
    }

    public void changeState() {
        previousState = new MagnetMemento(this, state);

        int change = 0;
        if(state == 0) {
            change = 1;
        } else if (state == states - 1) {
            change = -1;
        } else {
            change = randomInt(2) == 0 ? -1 : 1;
        }

        this.state += change;
    }

    public void restoreState() {
        this.previousState.restore();
    }

    private int randomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }


    private class MagnetMemento {
        private Magnet magnet;
        private int state;

        public MagnetMemento(Magnet magnet, int state) {
            this.magnet = magnet;
            this.state = state;
        }

        public void restore() {
            this.magnet.state = state;
        }
    }


}
