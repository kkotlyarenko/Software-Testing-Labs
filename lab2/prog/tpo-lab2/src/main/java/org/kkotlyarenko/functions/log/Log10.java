package org.kkotlyarenko.functions.log;

import org.kkotlyarenko.functions.MathFunction;

public class Log10 implements MathFunction {

    private final Ln ln;
    private static final double LN_10 = 2.302585092994046;

    public Log10(Ln ln) {
        this.ln = ln;
    }

    public Log10() {
        this.ln = new Ln();
    }

    @Override
    public double calculate(double x, double epsilon) {
        if (x <= 0) {
            return Double.NaN;
        }
        return ln.calculate(x, epsilon) / LN_10;
    }
}
