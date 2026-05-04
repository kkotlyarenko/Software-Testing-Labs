package org.kkotlyarenko.functions;

import org.kkotlyarenko.functions.log.Ln;
import org.kkotlyarenko.functions.log.Log10;
import org.kkotlyarenko.functions.log.Log2;
import org.kkotlyarenko.functions.log.Log5;
import org.kkotlyarenko.functions.trig.*;

/**
 * x <= 0: (((((((((((sin(x) - sin(x)) / sin(x)) - sec(x)) ^ 3) ^ 3) + cos(x)) / csc(x)) ^ 2) * sec(x)) * (sec(x) / (((tan(x) + tan(x)) - (tan(x) - ((tan(x) ^ 3) ^ 3))) + ((tan(x) ^ 2) - cos(x))))) - ((((tan(x) * sin(x)) / sec(x)) - (cot(x) * sec(x))) ^ 2))
 * x > 0: (((((log_5(x) * log_10(x)) + log_2(x)) + ln(x)) * log_5(x)) / (log_10(x) ^ 3))
 */
public class FunctionSystem implements MathFunction {

    private final Sin sin;
    private final Cos cos;
    private final Tan tan;
    private final Cot cot;
    private final Sec sec;
    private final Csc csc;
    private final Ln ln;
    private final Log2 log2;
    private final Log5 log5;
    private final Log10 log10;

    public FunctionSystem(Sin sin, Cos cos, Tan tan, Cot cot, Sec sec, Csc csc,
                          Ln ln, Log2 log2, Log5 log5, Log10 log10) {
        this.sin = sin;
        this.cos = cos;
        this.tan = tan;
        this.cot = cot;
        this.sec = sec;
        this.csc = csc;
        this.ln = ln;
        this.log2 = log2;
        this.log5 = log5;
        this.log10 = log10;
    }

    public FunctionSystem(Ln ln, Log2 log2, Log5 log5, Log10 log10) {
        this.ln = ln;
        this.log2 = log2;
        this.log5 = log5;
        this.log10 = log10;

        this.sin = new Sin();
        this.cos = new Cos();
        this.tan = new Tan();
        this.cot = new Cot();
        this.sec = new Sec();
        this.csc = new Csc();
    }

    public FunctionSystem(Sin sin, Cos cos, Tan tan, Cot cot, Sec sec, Csc csc) {
        this.sin = sin;
        this.cos = cos;
        this.tan = tan;
        this.cot = cot;
        this.sec = sec;
        this.csc = csc;

        this.ln = new Ln();
        this.log2 = new Log2();
        this.log5 = new Log5();
        this.log10 = new Log10();
    }

    public FunctionSystem() {
        this.sin = new Sin();
        this.cos = new Cos(sin);
        this.tan = new Tan(sin, cos);
        this.cot = new Cot(sin, cos);
        this.sec = new Sec(cos);
        this.csc = new Csc(sin);
        this.ln = new Ln();
        this.log2 = new Log2(ln);
        this.log5 = new Log5(ln);
        this.log10 = new Log10(ln);
    }

    @Override
    public double calculate(double x, double epsilon) {
        if (x <= 0) {
            return calculateNegative(x, epsilon);
        } else {
            return calculatePositive(x, epsilon);
        }
    }

    /**
     * x <= 0: (((((((((((sin(x) - sin(x)) / sin(x)) - sec(x)) ^ 3) ^ 3) + cos(x)) / csc(x)) ^ 2) * sec(x)) * (sec(x) / (((tan(x) + tan(x)) - (tan(x) - ((tan(x) ^ 3) ^ 3))) + ((tan(x) ^ 2) - cos(x))))) - ((((tan(x) * sin(x)) / sec(x)) - (cot(x) * sec(x))) ^ 2))
     */
    private double calculateNegative(double x, double epsilon) {
        double sinX = sin.calculate(x, epsilon);
        double cosX = cos.calculate(x, epsilon);
        double tanX = tan.calculate(x, epsilon);
        double cotX = cot.calculate(x, epsilon);
        double secX = sec.calculate(x, epsilon);
        double cscX = csc.calculate(x, epsilon);

        if (Double.isNaN(sinX) || Double.isNaN(cosX) || Double.isNaN(tanX) ||
            Double.isNaN(cotX) || Double.isNaN(secX) || Double.isNaN(cscX)) {
            return Double.NaN;
        }

        // (sin(x) - sin(x)) / sin(x) = 0 / sin(x) = 0
        if (Math.abs(sinX) < epsilon) {
            return Double.NaN;
        }
        double part1 = (sinX - sinX) / sinX; // = 0

        // (0 - sec(x)) ^ 3 = (-sec(x)) ^ 3
        double part2 = Math.pow(part1 - secX, 3);

        // (part2) ^ 3
        double part3 = Math.pow(part2, 3);

        // part3 + cos(x)
        double part4 = part3 + cosX;

        // part4 / csc(x)
        double part5 = part4 / cscX;

        // part5 ^ 2
        double part6 = Math.pow(part5, 2);

        // part6 * sec(x)
        double part7 = part6 * secX;

        // ((tan(x) + tan(x)) - (tan(x) - ((tan(x) ^ 3) ^ 3))) + ((tan(x) ^ 2) - cos(x))
        // tan(x) + tan(x) = 2*tan(x)
        // tan(x)^3 = tanX^3
        // (tan(x)^3)^3 = tanX^9
        double tan3 = Math.pow(tanX, 3);
        double tan9 = Math.pow(tan3, 3);
        double tan2 = Math.pow(tanX, 2);

        // (tan(x) + tan(x)) = 2*tan(x)
        double leftPart = 2 * tanX;
        // (tan(x) - ((tan(x)^3)^3)) = tan(x) - tan^9(x)
        double rightPart = tanX - tan9;
        // leftPart - rightPart = 2*tan(x) - (tan(x) - tan^9(x)) = 2*tan(x) - tan(x) + tan^9(x) = tan(x) + tan^9(x)
        double firstPart = leftPart - rightPart;

        // (tan(x)^2) - cos(x)
        double secondPart = tan2 - cosX;

        // firstPart + secondPart
        double denominator = firstPart + secondPart;

        if (Math.abs(denominator) < epsilon) {
            return Double.NaN;
        }

        // sec(x) / denominator
        double part8 = secX / denominator;

        // part7 * part8
        double part9 = part7 * part8;

        // ((((tan(x) * sin(x)) / sec(x)) - (cot(x) * sec(x))) ^ 2)
        // (tan(x) * sin(x)) / sec(x) = tan(x) * sin(x) * cos(x) = sin(x)^2 / cos(x) * cos(x) = sin(x)^2
        double term1 = (tanX * sinX) / secX;
        // cot(x) * sec(x) = cos(x) / sin(x) * 1/cos(x) = 1/sin(x) = csc(x)
        double term2 = cotX * secX;
        double part10 = Math.pow(term1 - term2, 2);

        return part9 - part10;
    }

    /**
     * x > 0: (((((log_5(x) * log_10(x)) + log_2(x)) + ln(x)) * log_5(x)) / (log_10(x) ^ 3))
     */
    private double calculatePositive(double x, double epsilon) {
        double lnX = ln.calculate(x, epsilon);
        double log2X = log2.calculate(x, epsilon);
        double log5X = log5.calculate(x, epsilon);
        double log10X = log10.calculate(x, epsilon);

        if (Double.isNaN(lnX) || Double.isNaN(log2X) ||
            Double.isNaN(log5X) || Double.isNaN(log10X)) {
            return Double.NaN;
        }

        // (log_5(x) * log_10(x)) + log_2(x)
        double part1 = (log5X * log10X) + log2X;

        // part1 + ln(x)
        double part2 = part1 + lnX;

        // part2 * log_5(x)
        double part3 = part2 * log5X;

        // log_10(x) ^ 3
        double log10Cubed = Math.pow(log10X, 3);

        if (Math.abs(log10Cubed) < epsilon) {
            return Double.NaN;
        }

        return part3 / log10Cubed;
    }
}
