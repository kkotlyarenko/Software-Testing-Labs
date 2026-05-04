package org.kkotlyarenko.functions.unit.trig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kkotlyarenko.functions.config.TestConfig;
import org.kkotlyarenko.functions.tables.TrigTable;
import org.kkotlyarenko.functions.trig.Sin;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SinTest {
    static Stream<Arguments> data() {
        return TrigTable.SIN.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Sin sin;

    @BeforeEach
    public void setup() {
        sin = new Sin();
    }

    @ParameterizedTest(name = "sin({0}) = {1}")
    @MethodSource("data")
    public void testSin(double x, double expected) {
        double result = sin.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "sin({0}) periodicity test")
    @MethodSource("data")
    public void testSinPeriodicity(double x, double expected) {
        double result1 = sin.calculate(x, TestConfig.EPSILON);
        double result2 = sin.calculate(x + 2 * Math.PI, TestConfig.EPSILON);
        assertEquals(result1, result2, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "sin(-{0}) = -sin({0}) oddness test")
    @ValueSource(doubles = {0.5, 1.0, 1.5, 2.0, 2.5})
    void testSinOddFunction(double x) {
        double sinX = sin.calculate(x, TestConfig.EPSILON);
        double sinMinusX = sin.calculate(-x, TestConfig.EPSILON);
        assertEquals(-sinX, sinMinusX, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "sin({0}) with very small epsilon to test iteration limit")
    @ValueSource(doubles = {0.1, 1.0})
    void testSinWithVerySmallEpsilon(double x) {
        double result = sin.calculate(x, 1e-323);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
    }

    @ParameterizedTest(name = "sin({0}) with very large angles to test normalization")
    @ValueSource(doubles = {6.283185307179586, 12.566370614359172, 18.84955592153876      // 2π, 4π, 6π
                            -6.283185307179586, -12.566370614359172, -18.84955592153876}) // -2π, -4π, -6π
    void testSinWithVeryLargeAngles(double x) {
        double result = sin.calculate(x, TestConfig.EPSILON);
        assertEquals(0, result, TestConfig.DELTA);
    }
}
