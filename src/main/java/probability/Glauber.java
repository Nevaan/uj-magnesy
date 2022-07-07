package probability;

public class Glauber implements ProbabilityAlgorithm {
    @Override
    public double getProbability(double deltaE, double T) {
        //todo: superclass
        double kB = 1.380649 * Math.pow(10, -23);

        return Math.exp(-deltaE / (kB * T)) / ( 1 + Math.exp( -deltaE / (kB * T) ) );
    }
}
