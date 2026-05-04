package org.kkotlyarenko.functions.trig;

import org.kkotlyarenko.functions.MathFunction;

public class Cot implements MathFunction {

    private final Sin sin;
    private final Cos cos;

    public Cot(Sin sin, Cos cos) {
        this.sin = sin;
        this.cos = cos;
    }

    public Cot(Sin sin) {
        this.sin = sin;
        this.cos = new Cos();
    }

    public Cot(Cos cos) {
        this.sin = new Sin();
        this.cos = cos;
    }

    public Cot() {
        this.sin = new Sin();
        this.cos = new Cos(this.sin);
    }

    @Override
    public double calculate(double x, double epsilon) {
        double sinValue = sin.calculate(x, epsilon);
        if (Math.abs(sinValue) < epsilon) {
            return Double.NaN;
        }
        return cos.calculate(x, epsilon) / sinValue;
    }
}
