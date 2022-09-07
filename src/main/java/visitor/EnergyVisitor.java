package visitor;

import magnet.Magnet;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class EnergyVisitor extends AbstractVisitor<Double> {

    private final List<Double> parameters;
    private final double externalFieldAngle;

    public EnergyVisitor(int states, List<Double> parameters, double externalFieldAngle) {
        super(states);
        this.parameters = parameters;
        this.externalFieldAngle = externalFieldAngle;
    }

    @Override
    public Double visit(Magnet magnet) {
        double totalMagnetEnergy = 0.0;
        totalMagnetEnergy += 0.5 * countInternalMagnetEnergy(magnet);
        totalMagnetEnergy -= parameters.get(0) * Math.cos(countAngle(magnet.getState()) - this.externalFieldAngle);
        return totalMagnetEnergy;
    }

    private double countInternalMagnetEnergy(Magnet magnet) {
        double internalMagnetEnergy = 0;

        Map<Integer, List<Magnet>> neighbours = magnet.getNeighbours();
        List<Double> neighbourParams = parameters.subList(1, parameters.size());
        ListIterator<Double> iterator = neighbourParams.listIterator();

        while (iterator.hasNext()) {
            int parameterIndex = iterator.nextIndex();
            Double neighbourParameter = iterator.next();
            List<Magnet> nLevelNeighbours = neighbours.get(parameterIndex + 1);

            for(Magnet neighbour : nLevelNeighbours) {
                internalMagnetEnergy -= neighbourParameter * Math.cos(countAngle(magnet.getState()) - countAngle(neighbour.getState()));
            }
        }

        return internalMagnetEnergy;
    }

}
