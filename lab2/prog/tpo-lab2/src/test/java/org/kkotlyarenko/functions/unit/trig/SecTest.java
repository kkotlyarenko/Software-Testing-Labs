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
import org.kkotlyarenko.functions.trig.Sec;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.kkotlyarenko.functions.config.TestConfig.DELTA;

public class SecTest {
    static Stream<Arguments> data() {
        return TrigTable.SEC.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Sec sec;

    @BeforeEach
    public void setup() {
        sec = new Sec();
    }

    @ParameterizedTest(name = "sec({0}) = {1}")
    @MethodSource("data")
    public void testSec(double x, double expected) {
        double result = sec.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, DELTA);
    }

    @ParameterizedTest(name = "sec({0}) = {1} with mocked Cos")
    @MethodSource("data")
    public void testSecWithMockedCos(double x, double expected) {
        Cos cosStub = StubFactory.createStub(Cos.class, TrigTable.COS);
        Sec secWithMock = new Sec(cosStub);

        double result = secWithMock.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, DELTA);
    }

    @ParameterizedTest(name = "sec({0}) should be NaN at asymptotes")
    @ValueSource(doubles = {1.5707963267948966, 4.71238898038469})
    void testSecAsymptotes(double x) {
        double result = sec.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result) || Math.abs(result) > 1e6);
    }

    @ParameterizedTest(name = "sec(-{0}) = sec({0}) evenness test")
    @ValueSource(doubles = {0.5, 0.7, 1.0, 1.2})
    void testSecEvenFunction(double x) {
        double secX = sec.calculate(x, TestConfig.EPSILON);
        double secMinusX = sec.calculate(-x, TestConfig.EPSILON);
        assertEquals(secX, secMinusX, DELTA);
    }
}
