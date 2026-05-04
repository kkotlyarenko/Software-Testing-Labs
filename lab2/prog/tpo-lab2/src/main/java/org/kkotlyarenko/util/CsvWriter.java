package org.kkotlyarenko.util;

import org.kkotlyarenko.functions.MathFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {

    private final String separator;

    public CsvWriter(String separator) {
        this.separator = separator;
    }

    public CsvWriter() {
        this.separator = ",";
    }

    public void write(String filename, MathFunction function,
                      double start, double end, double step, double epsilon) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("X" + separator + "Result");

            double x = start;
            while (x <= end) {
                double result = function.calculate(x, epsilon);
                writer.println(formatDouble(x) + separator + formatDouble(result));
                x += step;
            }
        }
    }

    private String formatDouble(double value) {
        if (Double.isNaN(value)) {
            return "NaN";
        }
        if (Double.isInfinite(value)) {
            return value > 0 ? "Infinity" : "-Infinity";
        }
        return String.format("%.10f", value);
    }
}
