package visitor;

import magnet.Magnet;

import java.util.List;
import java.util.Map;

public class NearestNeighborVisitor extends AbstractVisitor<Double> {

    public NearestNeighborVisitor(int states) {
        super(states);
    }

    @Override
    public Double visit(Magnet magnet) {
        double order = 0.0;
        Map<Integer, List<Magnet>> neighbors = magnet.getNeighbors();
        List<Magnet> levelOneNeighbors = neighbors.get(1);
        for (Magnet neighborMagnet: levelOneNeighbors) {
            order += Math.cos(countAngle(magnet.getState()) - countAngle(neighborMagnet.getState()));
        }
        return order;
    }
}
