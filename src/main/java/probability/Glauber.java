package probability;

public class Glauber implements ProbabilityAlgorithm {
    @Override
    public double getProbability(double energyDelta, double temperature) {
        double mathExp = Math.exp(-energyDelta /temperature);
        return mathExp / (1 + mathExp);
    }
}
