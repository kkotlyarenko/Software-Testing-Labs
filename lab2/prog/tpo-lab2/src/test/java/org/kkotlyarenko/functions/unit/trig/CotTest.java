package org.kkotlyarenko.functions.unit.trig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kkotlyarenko.functions.config.TestConfig;
import org.kkotlyarenko.functions.stubs.StubFactory;
import org.kkotlyarenko.functions.tables.TrigTable;
import org.kkotlyarenko.functions.trig.Cos;
import org.kkotlyarenko.functions.trig.Cot;
import org.kkotlyarenko.functions.trig.Sin;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CotTest {
    static Stream<Arguments> data() {
        return TrigTable.COT.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Cot cot;

    @BeforeEach
    public void setup() {
        cot = new Cot();
    }

    @ParameterizedTest(name = "cot({0}) = {1}")
    @MethodSource("data")
    public void testCot(double x, double expected) {
        double result = cot.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "cot({0}) = {1} with mocked Sin")
    @MethodSource("data")
    public void testCotWithMockedSin(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigTable.SIN);
        Cot cotWithMockedSin = new Cot(sinStub);

        double result = cotWithMockedSin.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "cot({0}) = {1} with mocked Cos")
    @MethodSource("data")
    public void testCotWithMockedCos(double x, double expected) {
        Cos cosStub = StubFactory.createStub(Cos.class, TrigTable.COS);
        Cot cotWithMockedCos = new Cot(cosStub);

        double result = cotWithMockedCos.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "cot({0}) = {1} with mocked Sin")
    @MethodSource("data")
    public void testCotWithMockedSinAndCos(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigTable.SIN);
        Cos cosStub = StubFactory.createStub(Cos.class, TrigTable.COS);
        Cot cotWithMockedSinAndCos = new Cot(sinStub, cosStub);

        double result = cotWithMockedSinAndCos.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "cot({0}) should be NaN at asymptotes")
    @ValueSource(doubles = {0, 3.141592653589793, 6.283185307179586})
    void testCotAsymptotes(double x) {
        double result = cot.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result) || Math.abs(result) > 1e6);
    }

    @ParameterizedTest(name = "tan(-{0}) = -tan({0}) oddness test")
    @ValueSource(doubles = {0.5, 0.7, 1.0, 1.2})
    void testTanOddFunction(double x) {
        double tanX = cot.calculate(x, TestConfig.EPSILON);
        double tanMinusX = cot.calculate(-x, TestConfig.EPSILON);
        assertEquals(-tanX, tanMinusX, TestConfig.DELTA);
    }
}
