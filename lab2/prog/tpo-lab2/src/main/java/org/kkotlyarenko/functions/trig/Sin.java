package org.kkotlyarenko.functions.trig;

import org.kkotlyarenko.functions.MathFunction;

public class Sin implements MathFunction {

    @Override
    public double calculate(double x, double epsilon) {
        x = normalizeAngle(x);

        double result = 0;
        double term = x;
        int n = 1;

        while (Math.abs(term) > epsilon) {
            result += term;
            term = -term * x * x / ((2 * n) * (2 * n + 1));
            n++;

            if (n > 1000) break;
        }

        return result;
    }

    private double normalizeAngle(double x) {
        double PI = 3.14159265358979323846;
        double TWO_PI = 2 * PI;

        while (x > PI) {
            x -= TWO_PI;
        }
        while (x < -PI) {
            x += TWO_PI;
        }

        return x;
    }
}
