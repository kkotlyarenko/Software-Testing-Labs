package org.kkotlyarenko.functions.trig;

import org.kkotlyarenko.functions.MathFunction;

public class Sec implements MathFunction {

    private final Cos cos;

    public Sec(Cos cos) {
        this.cos = cos;
    }

    public Sec() {
        this.cos = new Cos();
    }

    @Override
    public double calculate(double x, double epsilon) {
        double cosValue = cos.calculate(x, epsilon);
        if (Math.abs(cosValue) < epsilon) {
            return Double.NaN;
        }
        return 1.0 / cosValue;
    }
}
