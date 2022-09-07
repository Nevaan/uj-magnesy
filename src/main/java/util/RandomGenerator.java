package util;

import java.util.Random;

public class RandomGenerator {

  private static Random random = new Random();

  private RandomGenerator() {
  }

  public static Integer getInt(int maxValue) {
    return random.nextInt(maxValue);
  }

  public static Double getDouble() {
    return random.nextDouble();
  }

}
