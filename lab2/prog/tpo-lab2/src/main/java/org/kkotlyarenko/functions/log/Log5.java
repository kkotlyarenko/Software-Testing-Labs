package org.kkotlyarenko.functions.log;

import org.kkotlyarenko.functions.MathFunction;

public class Log5 implements MathFunction {

    private final Ln ln;
    private static final double LN_5 = 1.6094379124341003;

    public Log5(Ln ln) {
        this.ln = ln;
    }

    public Log5() {
        this.ln = new Ln();
    }

    @Override
    public double calculate(double x, double epsilon) {
        if (x <= 0) {
            return Double.NaN;
        }
        return ln.calculate(x, epsilon) / LN_5;
    }
}
