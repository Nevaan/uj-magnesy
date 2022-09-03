package visitor;

import magnet.Magnet;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class EnergyVisitor extends AbstractVisitor<Double> {

    private List<Double> parameters;
    private double externalFieldAngle;

    public EnergyVisitor(int states, List<Double> parameters, double externalFieldAngle) {
        super(states);
        this.parameters = parameters;
        this.externalFieldAngle = externalFieldAngle;
    }

    @Override
    public Double visit(Magnet magnet) {
        double Etot = 0.0;
        Etot += 0.5 * countEi(magnet);
        Etot -= parameters.get(0) * Math.cos(countAngle(magnet.getState()) - this.externalFieldAngle);
        return Etot;
    }

    private double countEi(Magnet magnet) {
        double Ei = 0;

        Map<Integer, List<Magnet>> neighbours = magnet.getNeighbors();
        List<Double> neighborParams = parameters.subList(1, parameters.size());
        ListIterator<Double> iterator = neighborParams.listIterator();

        while (iterator.hasNext()) {
            int idx = iterator.nextIndex();
            Double Cn = iterator.next();
            List<Magnet> nLevelNeighbors = neighbours.get(idx + 1);

            for(Magnet neighbor : nLevelNeighbors) {
                Ei -= Cn * Math.cos(countAngle(magnet.getState()) - countAngle(neighbor.getState()));
            }
        }

        return Ei;
    }

}
