package org.kkotlyarenko.functions.unit.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kkotlyarenko.functions.config.TestConfig;
import org.kkotlyarenko.functions.log.Ln;
import org.kkotlyarenko.functions.log.Log5;
import org.kkotlyarenko.functions.stubs.StubFactory;
import org.kkotlyarenko.functions.tables.LogTable;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Log5Test {
    static Stream<Arguments> data() {
        return LogTable.LOG5.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Log5 log5;

    @BeforeEach
    public void setup() {
        log5 = new Log5();
    }

    @ParameterizedTest(name = "log5({0}) = {1} with real Ln")
    @MethodSource("data")
    public void log5WithRealLn(double x, double expected) {
        double result = log5.calculate(x, TestConfig.EPSILON);

        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "log5({0}) = {1} with mocked Ln")
    @MethodSource("data")
    public void log5WithMockedLn(double x, double expected) {
        Ln lnStub = StubFactory.createStub(Ln.class, LogTable.LN);
        Log5 log5WithMock = new Log5(lnStub);

        double result = log5WithMock.calculate(x, TestConfig.EPSILON);

        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "log5({0}) should be NaN for non-positive values")
    @ValueSource(doubles = {0, -1, -5, -100, -0.0001})
    void testLog5NonPositive(double x) {
        double result = log5.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "log5({0} should be Nan for NaN values")
    @ValueSource(doubles = {Double.NaN})
    void testLog5NaN(double x) {
         double result = log5.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "log5() with invalid epsilon {0} should be NaN")
    @ValueSource(doubles = {0, -1e-6, Double.NaN})
    void testInvalidEpsilon(double eps) {
        double result = log5.calculate(2.0, eps);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "log5({0}) with very small epsilon to test iteration limit")
    @ValueSource(doubles = {0.5, 2.0, 10.0})
    void testLnWithVerySmallEpsilon(double x) {
        double result = log5.calculate(x, 1e-100);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
    }

    @ParameterizedTest(name = "log5({0}) with epsilon triggering break")
    @ValueSource(doubles = {1.4999, 0.5001})
    void testLnEpsilon(double x) {
        double result = log5.calculate(x, 1e-200);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
    }
}
