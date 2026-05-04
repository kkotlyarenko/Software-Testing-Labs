package org.kkotlyarenko.functions.trig;

import org.kkotlyarenko.functions.MathFunction;

public class Csc implements MathFunction {

    private final Sin sin;

    public Csc(Sin sin) {
        this.sin = sin;
    }

    public Csc() {
        this.sin = new Sin();
    }

    @Override
    public double calculate(double x, double epsilon) {
        double sinValue = sin.calculate(x, epsilon);
        if (Math.abs(sinValue) < epsilon) {
            return Double.NaN;
        }
        return 1.0 / sinValue;
    }
}
