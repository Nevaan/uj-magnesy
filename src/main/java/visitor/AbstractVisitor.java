package visitor;

import magnet.Magnet;

public abstract class AbstractVisitor<T> {

    private int states;

    public AbstractVisitor(int states) {
        this.states = states;
    }

    protected double countAngle(int angleAsInteger) {
        return 2 * Math.PI * angleAsInteger / states;
    }

    public abstract T visit(Magnet magnet);

}
