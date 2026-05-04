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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CosTest {
    static Stream<Arguments> data() {
        return TrigTable.COS.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Cos cos;

    @BeforeEach
    public void setup() {
        cos = new Cos();
    }

    @ParameterizedTest(name = "cos({0}) = {1}")
    @MethodSource("data")
    public void testCos(double x, double expected) {
        double result = cos.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "cos({0}) = {1} with mocked Sin")
    @MethodSource("data")
    public void testCosWithMock(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigTable.SIN);
        Cos cosWithMock = new Cos(sinStub);

        double result = cosWithMock.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "cos({0}) periodicity test")
    @MethodSource("data")
    public void testCosPeriodicity(double x, double expected) {
        double result1 = cos.calculate(x, TestConfig.EPSILON);
        double result2 = cos.calculate(x + 2 * Math.PI, TestConfig.EPSILON);
        assertEquals(result1, result2, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "cos(-{0}) = cos({0}) evenness test")
    @ValueSource(doubles = {0.5, 1.0, 1.5, 2.0, 2.5})
    void testCosEvenFunction(double x) {
        double cosX = cos.calculate(x, TestConfig.EPSILON);
        double cosMinusX = cos.calculate(-x, TestConfig.EPSILON);
        assertEquals(cosX, cosMinusX, TestConfig.DELTA);
    }
}
