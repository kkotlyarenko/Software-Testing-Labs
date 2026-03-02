package org.kkotlyarenko.math;

public class ArcSinSeries {
    public static double arcsin(double x, int terms) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("x must be finite and in [-1, 1]");
        }
        if (x < -1 || x > 1) {
            throw new IllegalArgumentException("x must be in [-1, 1]");
        }
        if (terms <= 0) {
            throw new IllegalArgumentException("terms must be positive");
        }

        double result = 0.0;
        double numerator = x;
        double coeff = 1.0;

        for (int n = 0; n < terms; n++) {
            if (n > 0) {
                coeff *= (2.0 * n - 1) / (2.0 * n);
                coeff *= (2.0 * n - 1) / (2.0 * n + 1);
                numerator *= x * x;
            }
            result += coeff * numerator;
        }

        return result;
    }
}