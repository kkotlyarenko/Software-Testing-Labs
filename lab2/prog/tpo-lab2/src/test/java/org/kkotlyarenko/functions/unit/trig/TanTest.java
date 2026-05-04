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
import org.kkotlyarenko.functions.trig.Sin;
import org.kkotlyarenko.functions.trig.Tan;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TanTest {
    static Stream<Arguments> data() {
        return TrigTable.TAN.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Tan tan;

    @BeforeEach
    public void setup() {
        tan = new Tan();
    }

    @ParameterizedTest(name = "tan({0}) = {1}")
    @MethodSource("data")
    public void testTan(double x, double expected) {
        double result = tan.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "tan({0}) = {1} with mocked Sin")
    @MethodSource("data")
    public void testTanWithMockedSin(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigTable.SIN);
        Tan tanWithMockedSin = new Tan(sinStub);

        double result = tanWithMockedSin.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "tan({0}) = {1} with mocked Cos")
    @MethodSource("data")
    public void testTanWithMockedCos(double x, double expected) {
        Cos cosStub = StubFactory.createStub(Cos.class, TrigTable.COS);
        Tan tanWithMockedCos = new Tan(cosStub);

        double result = tanWithMockedCos.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "tan({0}) = {1} with mocked Sin and Cos")
    @MethodSource("data")
    public void testTanWithMockedSinAndCos(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigTable.SIN);
        Cos cosStub = StubFactory.createStub(Cos.class, TrigTable.COS);
        Tan tanWithMockedSinAndCos = new Tan(sinStub, cosStub);

        double result = tanWithMockedSinAndCos.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "tan({0}) should be NaN at asymptotes")
    @ValueSource(doubles = {1.5707963267948966, 4.71238898038469})
    void testTanAsymptotes(double x) {
        double result = tan.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result) || Math.abs(result) > 1e6);
    }

    @ParameterizedTest(name = "tan(-{0}) = -tan({0}) oddness test")
    @ValueSource(doubles = {0.5, 0.7, 1.0, 1.2})
    void testTanOddFunction(double x) {
        double tanX = tan.calculate(x, TestConfig.EPSILON);
        double tanMinusX = tan.calculate(-x, TestConfig.EPSILON);
        assertEquals(-tanX, tanMinusX, TestConfig.DELTA);
    }
}
