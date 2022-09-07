package probability;

public class Metropolis implements ProbabilityAlgorithm {
    @Override
    public double getProbability(double energyDelta, double temperature) {
        if (energyDelta > 0) {
            return Math.exp( (-energyDelta / temperature) );
        } else {
            return 1;
        }
    }
}
