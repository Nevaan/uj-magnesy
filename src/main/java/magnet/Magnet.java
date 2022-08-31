package magnet;

public class Magnet {

    private int state;

    public Magnet(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void changeState(int change) {
        this.state += change;
    }

}
