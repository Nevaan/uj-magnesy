package probability;

public class Metropolis implements ProbabilityAlgorithm {
    @Override
    public double getProbability(double deltaE, double T) {
        if (deltaE > 0) {
            return Math.exp( (-deltaE / T) );
        } else {
            return 1;
        }
    }
}
