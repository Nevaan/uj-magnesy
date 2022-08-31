package magnet;

import java.util.List;
import java.util.Map;

public class Magnet {

    private int state;
    private Map<Integer, List<Magnet>> neighbors;
    private MagnetMemento previousState;

    public Magnet(int state) {
        this.state = state;
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

    public void changeState(int change) {
        previousState = new MagnetMemento(this, state);
        this.state += change;
    }

    public void restoreState() {
        this.previousState.restore();
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
