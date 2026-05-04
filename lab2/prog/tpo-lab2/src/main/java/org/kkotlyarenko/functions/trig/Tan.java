package org.kkotlyarenko.functions.trig;

import org.kkotlyarenko.functions.MathFunction;

public class Tan implements MathFunction {

    private final Sin sin;
    private final Cos cos;

    public Tan(Sin sin, Cos cos) {
        this.sin = sin;
        this.cos = cos;
    }

    public Tan(Sin sin) {
        this.sin = sin;
        this.cos = new Cos();
    }

    public Tan(Cos cos) {
        this.sin = new Sin();
        this.cos = cos;
    }

    public Tan() {
        this.sin = new Sin();
        this.cos = new Cos(this.sin);
    }

    @Override
    public double calculate(double x, double epsilon) {
        double cosValue = cos.calculate(x, epsilon);
        if (Math.abs(cosValue) < epsilon) {
            return Double.NaN;
        }
        return sin.calculate(x, epsilon) / cosValue;
    }
}
