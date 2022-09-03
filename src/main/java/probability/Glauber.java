package probability;

public class Glauber implements ProbabilityAlgorithm {
    @Override
    public double getProbability(double deltaE, double T) {
        double mathExp = Math.exp(-deltaE/T);
        return mathExp / (1 + mathExp);
    }
}
