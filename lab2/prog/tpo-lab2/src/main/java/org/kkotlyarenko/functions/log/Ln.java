package org.kkotlyarenko.functions.log;

import org.kkotlyarenko.functions.MathFunction;

public class Ln implements MathFunction {

    @Override
    public double calculate(double x, double epsilon) {
        if (Double.isNaN(x) || x <= 0) {
            return Double.NaN;
        }

        if (Double.isNaN(epsilon) || epsilon <= 0) {
            return Double.NaN;
        }

        // ln(x) = ln(x/e^n) + n
        int powerAdjust = 0;
        double E = 2.718281828459045;

        while (x > 1.5) {
            x /= E;
            powerAdjust++;
        }
        while (x < 0.5) {
            x *= E;
            powerAdjust--;
        }

        //ln(x) = 2 * (t + t^3/3 + t^5/5 + ...)
        double t = (x - 1) / (x + 1);
        double tSquared = t * t;

        double result = 0;
        double term = t;
        int n = 1;

        while (Math.abs(term) > epsilon / 2) {
            result += term;
            term *= tSquared * (2 * n - 1) / (2 * n + 1);
            n++;

            if (n > 1000) break;
        }

        return 2 * result + powerAdjust;
    }
}
