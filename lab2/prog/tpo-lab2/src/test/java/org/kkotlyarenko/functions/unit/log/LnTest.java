package org.kkotlyarenko.functions.unit.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kkotlyarenko.functions.config.TestConfig;
import org.kkotlyarenko.functions.log.Ln;
import org.kkotlyarenko.functions.tables.LogTable;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ln Function Tests")
public class LnTest {
    static Stream<Arguments> data() {
        return LogTable.LN.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Ln ln;

    @BeforeEach
    public void setup() {
        ln = new Ln();
    }

    @ParameterizedTest(name = "ln({0}) = {1}")
    @MethodSource("data")
    void testLn(double x, double expected) {
        double actual = ln.calculate(x, TestConfig.EPSILON);

        assertEquals(expected, actual, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "ln({0}) should be NaN for non-positive values")
    @ValueSource(doubles = {0, -1, -5, -100, -0.0001})
    void testLnNonPositive(double x) {
        double result = ln.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "ln({0} should be Nan for NaN values")
    @ValueSource(doubles = {Double.NaN})
    void testLnNaN(double x) {
        double result = ln.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "ln() with invalid epsilon {0} should be NaN")
    @ValueSource(doubles = {0, -1e-6, Double.NaN})
    void testInvalidEpsilon(double eps) {
        double result = ln.calculate(2.0, eps);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "ln({0}) with very small epsilon to test iteration limit")
    @ValueSource(doubles = {0.5, 2.0, 10.0})
    void testLnWithVerySmallEpsilon(double x) {
        double result = ln.calculate(x, 1e-100);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
    }

    @ParameterizedTest(name = "ln({0}) with epsilon triggering break")
    @ValueSource(doubles = {1000, 1e-10, 1e-50, Double.MAX_VALUE})
    void testLnEpsilon(double x) {
        double result = ln.calculate(x, 1e-323);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
    }
}
