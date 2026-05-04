package org.kkotlyarenko.functions.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kkotlyarenko.functions.FunctionSystem;
import org.kkotlyarenko.functions.config.TestConfig;
import org.kkotlyarenko.functions.log.Ln;
import org.kkotlyarenko.functions.log.Log10;
import org.kkotlyarenko.functions.log.Log2;
import org.kkotlyarenko.functions.log.Log5;
import org.kkotlyarenko.functions.stubs.StubFactory;
import org.kkotlyarenko.functions.tables.system.LogSystemTable;
import org.kkotlyarenko.functions.tables.system.SystemTable;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LogLayerTest {
    static Stream<Arguments> data() {
        return SystemTable.LOG.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }


    @ParameterizedTest(name = "layer1: LogPart({0}) = {1} everything mocked")
    @MethodSource("data")
    void testLogLayer1(double x, double expected) {
        Ln lnStub = StubFactory.createStub(Ln.class, LogSystemTable.LN);
        Log2 log2Stub = StubFactory.createStub(Log2.class, LogSystemTable.LOG2);
        Log5 log5Stub = StubFactory.createStub(Log5.class, LogSystemTable.LOG5);
        Log10 log10Stub = StubFactory.createStub(Log10.class, LogSystemTable.LOG10);

        FunctionSystem logSystem = new FunctionSystem(lnStub, log2Stub, log5Stub, log10Stub);
        double result = logSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "layer2: LogPart({0}) = {1} ln mocked, while log2, log5, log10 are real")
    @MethodSource("data")
    void testLogLayer2(double x, double expected) {
        Ln lnStub = StubFactory.createStub(Ln.class, LogSystemTable.LN);
        Log2 log2 = new Log2();
        Log5 log5 = new Log5();
        Log10 log10 = new Log10();

        FunctionSystem logSystem = new FunctionSystem(lnStub, log2, log5, log10);
        double result = logSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "layer3: LogPart({0}) = {1} everything is real")
    @MethodSource("data")
    void testLogLayer3(double x, double expected) {
        Ln ln = new Ln();
        Log2 log2 = new Log2();
        Log5 log5 = new Log5();
        Log10 log10 = new Log10();

        FunctionSystem logSystem = new FunctionSystem(ln, log2, log5, log10);
        double result = logSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @Test()
    @DisplayName("If every module is Nan return Nan")
    void testNaN() {
        double x = 1.23;
        Ln lnBad = StubFactory.createNaNStub(Ln.class);
        Log2 log2Bad = StubFactory.createNaNStub(Log2.class);
        Log5 log5Bad = StubFactory.createNaNStub(Log5.class);
        Log10 log10Bad = StubFactory.createNaNStub(Log10.class);

        Ln lnStub = StubFactory.createStub(Ln.class, LogSystemTable.LN);
        Log2 log2Stub = StubFactory.createStub(Log2.class, LogSystemTable.LOG2);
        Log5 log5Stub = StubFactory.createStub(Log5.class, LogSystemTable.LOG5);
        Log10 log10Stub = StubFactory.createStub(Log10.class, LogSystemTable.LOG10);

        assertTrue(Double.isNaN(
                new FunctionSystem(lnBad, log2Stub, log5Stub, log10Stub)
                        .calculate(x, TestConfig.EPSILON)
        ));

        assertTrue(Double.isNaN(
                new FunctionSystem(lnStub, log2Bad, log5Stub, log10Stub)
                        .calculate(x, TestConfig.EPSILON)

        ));

        assertTrue(Double.isNaN(
                new FunctionSystem(lnStub, log2Stub, log5Bad, log10Stub)
                        .calculate(x, TestConfig.EPSILON)
        ));

        assertTrue(Double.isNaN(
                new FunctionSystem(lnStub, log2Stub, log5Stub, log10Bad)
                        .calculate(x, TestConfig.EPSILON)
        ));
    }
}
