package visitor;

import magnet.Magnet;
import util.Tuple;

public class OrderParameterVisitor extends AbstractVisitor<Tuple<Double, Double>> {

    public OrderParameterVisitor(int states) {
        super(states);
    }

    @Override
    public Tuple<Double, Double> visit(Magnet magnet) {
        double angle = countAngle(magnet.getState());
        return new Tuple(Math.cos(angle), Math.sin(angle));
    }
}
