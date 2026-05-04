package org.kkotlyarenko.functions.log;

import org.kkotlyarenko.functions.MathFunction;

public class Log2 implements MathFunction {

    private final Ln ln;
    private static final double LN_2 = 0.6931471805599453;

    public Log2(Ln ln) {
        this.ln = ln;
    }

    public Log2() {
        this.ln = new Ln();
    }

    @Override
    public double calculate(double x, double epsilon) {
        if (x <= 0) {
            return Double.NaN;
        }
        return ln.calculate(x, epsilon) / LN_2;
    }
}
