package fr.univtln.laure.service;

import java.util.Random;

public class DPUtils {
    /*
     * This class provides methods for differential privacy.
     * It includes methods to generate Laplace noise and to privatize ratings.
     */
    private static Random rand = new Random();

    public static double laplaceNoise(double epsilon) {
        double scale = 4.5/ epsilon;
        double u = 0.5 - rand.nextDouble();
        return -scale * Math.signum(u) * Math.log(1 - 2 * Math.abs(u));
    }

    public static double privatizeRating(double rating, double epsilon) {
        double noisy = rating + laplaceNoise(epsilon);
        noisy = Math.max(0, Math.min(5, noisy));
        return Math.round(noisy * 2) / 2.0;
    }
}
