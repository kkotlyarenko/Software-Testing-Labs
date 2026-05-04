package org.kkotlyarenko.functions.unit.trig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kkotlyarenko.functions.config.TestConfig;
import org.kkotlyarenko.functions.stubs.StubFactory;
import org.kkotlyarenko.functions.tables.TrigTable;
import org.kkotlyarenko.functions.trig.Csc;
import org.kkotlyarenko.functions.trig.Sin;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CscTest {
    static Stream<Arguments> data() {
        return TrigTable.CSC.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Csc csc;

    @BeforeEach
    public void setup() {
        csc = new Csc();

    }

    @ParameterizedTest(name = "csc({0}) = {1}")
    @MethodSource("data")
    public void testCsc(double x, double expected) {
        double result = csc.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "csc({0}) = {1} with mocked Sin")
    @MethodSource("data")
    public void testCscWithMockedSin(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigTable.SIN);
        Csc cscWithMock = new Csc(sinStub);

        double result = cscWithMock.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "csc({0}) should be NaN at asymptotes")
    @ValueSource(doubles = {0, 3.141592653589793, 6.283185307179586})
    void testCscAsymptotes(double x) {
        double result = csc.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result) || Math.abs(result) > 1e6);
    }

    @ParameterizedTest(name = "csc(-{0}) = -csc({0}) oddness test")
    @ValueSource(doubles = {0.5, 0.7, 1.0, 1.2})
    void testCscOddFunction(double x) {
        double cscX = csc.calculate(x, TestConfig.EPSILON);
        double cscMinusX = csc.calculate(-x, TestConfig.EPSILON);
        assertEquals(-cscX, cscMinusX, TestConfig.DELTA);
    }
}
