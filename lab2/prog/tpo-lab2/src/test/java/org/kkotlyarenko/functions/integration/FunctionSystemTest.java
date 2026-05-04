package org.kkotlyarenko.functions.integration;

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
import org.kkotlyarenko.functions.tables.system.TrigSystemTable;
import org.kkotlyarenko.functions.trig.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionSystemTest {
    static Stream<Arguments> data() {
        return Stream.of(SystemTable.TRIG, SystemTable.LOG)
                .flatMap(map -> map.entrySet().stream())
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    @ParameterizedTest(name = "Layer 1: FunctionSystem({0}) = {1} everything mocked")
    @MethodSource("data")
    public void testSystemLayer1(double x, double expected) {
        Ln lnStub = StubFactory.createStub(Ln.class, LogSystemTable.LN);
        Log2 log2Stub = StubFactory.createStub(Log2.class, LogSystemTable.LOG2);
        Log5 log5Stub = StubFactory.createStub(Log5.class, LogSystemTable.LOG5);
        Log10 log10Stub = StubFactory.createStub(Log10.class, LogSystemTable.LOG10);

        Sin sinStub = StubFactory.createStub(Sin.class, TrigSystemTable.SIN);
        Cos cosStub = StubFactory.createStub(Cos.class, TrigSystemTable.COS);
        Tan tanStub = StubFactory.createStub(Tan.class, TrigSystemTable.TAN);
        Cot cotStub = StubFactory.createStub(Cot.class, TrigSystemTable.COT);
        Sec secStub = StubFactory.createStub(Sec.class, TrigSystemTable.SEC);
        Csc cscStub = StubFactory.createStub(Csc.class, TrigSystemTable.CSC);

        FunctionSystem functionSystem = new FunctionSystem(
                sinStub, cosStub, tanStub, cotStub, secStub, cscStub,
                lnStub, log2Stub, log5Stub, log10Stub
        );

        double result = functionSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "Layer 2: FunctionSystem({0}) = {1} log functions are real, while trig functions are mocked")
    @MethodSource("data")
    public void testSystemLayer2Log(double x, double expected) {
        Ln ln = new Ln();
        Log2 log2 = new Log2();
        Log5 log5 = new Log5();
        Log10 log10 = new Log10();

        Sin sinStub = StubFactory.createStub(Sin.class, TrigSystemTable.SIN);
        Cos cosStub = StubFactory.createStub(Cos.class, TrigSystemTable.COS);
        Tan tanStub = StubFactory.createStub(Tan.class, TrigSystemTable.TAN);
        Cot cotStub = StubFactory.createStub(Cot.class, TrigSystemTable.COT);
        Sec secStub = StubFactory.createStub(Sec.class, TrigSystemTable.SEC);
        Csc cscStub = StubFactory.createStub(Csc.class, TrigSystemTable.CSC);

        FunctionSystem functionSystem = new FunctionSystem(
                sinStub, cosStub, tanStub, cotStub, secStub, cscStub,
                ln, log2, log5, log10
        );

        double result = functionSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "Layer 2: FunctionSystem({0}) = {1} trig functions are real, while log functions are mocked")
    @MethodSource("data")
    public void testSystemLayer2Trig(double x, double expected) {
        Ln lnStub = StubFactory.createStub(Ln.class, LogSystemTable.LN);
        Log2 log2Stub = StubFactory.createStub(Log2.class, LogSystemTable.LOG2);
        Log5 log5Stub = StubFactory.createStub(Log5.class, LogSystemTable.LOG5);
        Log10 log10Stub = StubFactory.createStub(Log10.class, LogSystemTable.LOG10);

        Sin sin = new Sin();
        Cos cos = new Cos();
        Tan tan = new Tan();
        Cot cot = new Cot();
        Sec sec = new Sec();
        Csc csc = new Csc();

        FunctionSystem functionSystem = new FunctionSystem(
                sin, cos, tan, cot, sec, csc,
                lnStub, log2Stub, log5Stub, log10Stub
        );

        double result = functionSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "Layer 3: FunctionSystem({0}) = {1} everything real")
    @MethodSource("data")
    public void testSystemLayer3(double x, double expected) {
        FunctionSystem functionSystem = new FunctionSystem();

        double result = functionSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }
}
