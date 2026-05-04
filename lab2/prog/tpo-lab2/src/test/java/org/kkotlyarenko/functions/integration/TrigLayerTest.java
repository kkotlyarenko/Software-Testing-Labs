package org.kkotlyarenko.functions.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kkotlyarenko.functions.FunctionSystem;
import org.kkotlyarenko.functions.config.TestConfig;
import org.kkotlyarenko.functions.stubs.StubFactory;
import org.kkotlyarenko.functions.tables.system.SystemTable;
import org.kkotlyarenko.functions.tables.system.TrigSystemTable;
import org.kkotlyarenko.functions.trig.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TrigLayerTest {
    static Stream<Arguments> data() {
        return SystemTable.TRIG.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }

    @ParameterizedTest(name = "layer1: TrigPart({0}) = {1} everything mocked")
    @MethodSource("data")
    void testTrigLayer1(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigSystemTable.SIN);
        Cos cosStub = StubFactory.createStub(Cos.class, TrigSystemTable.COS);
        Tan tanStub = StubFactory.createStub(Tan.class, TrigSystemTable.TAN);
        Cot cotStub = StubFactory.createStub(Cot.class, TrigSystemTable.COT);
        Sec secStub = StubFactory.createStub(Sec.class, TrigSystemTable.SEC);
        Csc cscStub = StubFactory.createStub(Csc.class, TrigSystemTable.CSC);

        FunctionSystem trigSystem = new FunctionSystem(sinStub, cosStub, tanStub, cotStub, secStub, cscStub);
        double result = trigSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "layer2: TrigPart({0}) = {1} tg, ctg, sec are real, while sin, cos, csc are mocked")
    @MethodSource("data")
    void testTrigLayer2(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigSystemTable.SIN);
        Cos cosStub = StubFactory.createStub(Cos.class, TrigSystemTable.COS);
        Csc cscStub = StubFactory.createStub(Csc.class, TrigSystemTable.CSC);

        Tan tan = new Tan();
        Cot cot = new Cot();
        Sec sec = new Sec();

        FunctionSystem trigSystem = new FunctionSystem(sinStub, cosStub, tan, cot, sec, cscStub);
        double result = trigSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "layer3: TrigPart({0}) = {1} cos, tg, ctg, sec, csc are real, while sin is mocked")
    @MethodSource("data")
    void testTrigLayer3(double x, double expected) {
        Sin sinStub = StubFactory.createStub(Sin.class, TrigSystemTable.SIN);

        Cos cos = new Cos();
        Csc csc = new Csc();
        Tan tan = new Tan();
        Cot cot = new Cot();
        Sec sec = new Sec();

        FunctionSystem trigSystem = new FunctionSystem(sinStub, cos, tan, cot, sec, csc);
        double result = trigSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @ParameterizedTest(name = "layer4: TrigPart({0}) = {1} everything is real")
    @MethodSource("data")
    void testTrigLayer4(double x, double expected) {
        Sin sin = new Sin();
        Cos cos = new Cos();
        Csc csc = new Csc();
        Tan tan = new Tan();
        Cot cot = new Cot();
        Sec sec = new Sec();

        FunctionSystem trigSystem = new FunctionSystem(sin, cos, tan, cot, sec, csc);
        double result = trigSystem.calculate(x, TestConfig.EPSILON);
        assertEquals(expected, result, TestConfig.DELTA);
    }

    @Test()
    @DisplayName("If every module is NaN return NaN")
    void testNaN() {
        double x = -1.23;
        Sin sinBad = StubFactory.createNaNStub(Sin.class);
        Cos cosBad = StubFactory.createNaNStub(Cos.class);
        Tan tanBad = StubFactory.createNaNStub(Tan.class);
        Cot cotBad = StubFactory.createNaNStub(Cot.class);
        Sec secBad = StubFactory.createNaNStub(Sec.class);
        Csc cscBad = StubFactory.createNaNStub(Csc.class);

        Sin sinStub = StubFactory.createStub(Sin.class, TrigSystemTable.SIN);
        Cos cosStub = StubFactory.createStub(Cos.class, TrigSystemTable.COS);
        Tan tanStub = StubFactory.createStub(Tan.class, TrigSystemTable.TAN);
        Cot cotStub = StubFactory.createStub(Cot.class, TrigSystemTable.COT);
        Sec secStub = StubFactory.createStub(Sec.class,  TrigSystemTable.SEC);
        Csc cscStub = StubFactory.createStub(Csc.class, TrigSystemTable.CSC);

        assertTrue(Double.isNaN(
                new FunctionSystem(sinBad, cosStub, tanStub, cotStub, secStub, cscStub)
                        .calculate(x, TestConfig.EPSILON)
        ));
        assertTrue(Double.isNaN(
                new FunctionSystem(sinStub, cosBad, tanStub, cotStub, secStub, cscStub)
                        .calculate(x, TestConfig.EPSILON)
        ));
        assertTrue(Double.isNaN(
                new FunctionSystem(sinStub, cosStub, tanBad, cotStub, secStub, cscStub)
                        .calculate(x, TestConfig.EPSILON)
        ));
        assertTrue(Double.isNaN(
                new FunctionSystem(sinStub, cosStub, tanStub, cotBad, secStub, cscStub)
                        .calculate(x, TestConfig.EPSILON)
        ));
        assertTrue(Double.isNaN(
                new FunctionSystem(sinStub, cosStub, tanStub, cotStub, secBad, cscStub)
                        .calculate(x, TestConfig.EPSILON)
        ));
        assertTrue(Double.isNaN(
                new FunctionSystem(sinStub, cosStub, tanStub, cotStub, secStub, cscBad)
                        .calculate(x, TestConfig.EPSILON)
        ));
    }
}
