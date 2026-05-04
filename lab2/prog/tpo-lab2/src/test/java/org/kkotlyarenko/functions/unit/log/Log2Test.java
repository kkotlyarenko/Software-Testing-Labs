package org.kkotlyarenko.functions.unit.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kkotlyarenko.functions.config.TestConfig;
import org.kkotlyarenko.functions.log.Ln;
import org.kkotlyarenko.functions.log.Log2;
import org.kkotlyarenko.functions.stubs.StubFactory;
import org.kkotlyarenko.functions.tables.LogTable;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class Log2Test {
    static Stream<Arguments> data() {
        return LogTable.LOG2.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    private Log2 log2;

    @BeforeEach
    public void setupTest() {
        log2 = new Log2();
    }

    @ParameterizedTest(name = "log2({0}) = {1} with real Ln")
    @MethodSource("data")
    public void log2WithRealLn(double x, double expected) {
        double result = log2.calculate(x, TestConfig.EPSILON);

        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "log2({0}) = {1} with mocked Ln")
    @MethodSource("data")
    public void log2WithMockedLn(double x, double expected) {
        Ln lnStub = StubFactory.createStub(Ln.class, LogTable.LN);
        Log2 log2WithMock = new Log2(lnStub);

        double result = log2WithMock.calculate(x, TestConfig.EPSILON);

        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "log2({0}) should be NaN for non-positive values")
    @ValueSource(doubles = {0, -1, -5, -100, -0.0001})
    void testLog2NonPositive(double x) {
        double result = log2.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "log2({0} should be Nan for NaN values")
    @ValueSource(doubles = {Double.NaN})
    void testLog2NaN(double x) {
        double result = log2.calculate(x, TestConfig.EPSILON);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "log2() with invalid epsilon {0} should be NaN")
    @ValueSource(doubles = {0, -1e-6, Double.NaN})
    void testInvalidEpsilon(double eps) {
        double result = log2.calculate(2.0, eps);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "log2({0}) with very small epsilon to test iteration limit")
    @ValueSource(doubles = {0.5, 2.0, 10.0})
    void testLnWithVerySmallEpsilon(double x) {
        double result = log2.calculate(x, 1e-100);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
    }

    @ParameterizedTest(name = "log2({0}) with epsilon triggering break")
    @ValueSource(doubles = {1.4999, 0.5001})
    void testLnEpsilon(double x) {
        double result = log2.calculate(x, 1e-200);
        assertFalse(Double.isNaN(result));
        assertTrue(Double.isFinite(result));
    }
}
