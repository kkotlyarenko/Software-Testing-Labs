package org.kkotlyarenko.functions.trig;

import org.kkotlyarenko.functions.MathFunction;

public class Cos implements MathFunction {

    private final Sin sin;

    public Cos(Sin sin) {
        this.sin = sin;
    }

    public Cos() {
        this.sin = new Sin();
    }

    @Override
    public double calculate(double x, double epsilon) {
        // cos(x) = sin(π/2 - x)
        double PI_2 = 1.5707963267948966;
        return sin.calculate(PI_2 - x, epsilon);
    }
}
