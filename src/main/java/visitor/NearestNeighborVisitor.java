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
        double nearestNeighbourOrder = 0.0;
        Map<Integer, List<Magnet>> neighbours = magnet.getNeighbours();
        List<Magnet> levelOneNeighbours = neighbours.get(1);
        for (Magnet neighbourMagnet: levelOneNeighbours) {
            nearestNeighbourOrder += Math.cos(countAngle(magnet.getState()) - countAngle(neighbourMagnet.getState()));
        }
        return nearestNeighbourOrder;
    }
}
