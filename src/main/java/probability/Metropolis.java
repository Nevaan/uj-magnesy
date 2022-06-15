package probability;

public class Metropolis implements ProbabilityAlgorithm {
    @Override
    public double getProbability(double deltaE, double T) {

        // todo: superclass
        double kB = 1.380649 * Math.pow(10, -23);

        if (deltaE > 0) {
            return Math.exp( (-deltaE / (kB * T)) );
        } else {
            return 1;
        }

    }
}
