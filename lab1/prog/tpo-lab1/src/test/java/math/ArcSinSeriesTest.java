package math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kkotlyarenko.math.ArcSinSeries;

import static org.junit.jupiter.api.Assertions.*;

class ArcSinSeriesTest {
    private static final double PI = 3.14159265358979323846;

    private static final double EPS = 1e-6;
    private static final int DEFAULT_TERMS = 50;

    @ParameterizedTest(name = "arcsin({0}) ≈ {1}")
    @DisplayName("Table values (positive)")
    @CsvSource({
            " 0.0,       0.0",
            " 0.5,       0.5235987755982988",
            " 0.7071067811865476, 0.7853981633974483",
            " 0.8660254037844386, 1.0471975511965976",
    })
    void testPositiveTableValues(double x, double expected) {
        assertEquals(expected, ArcSinSeries.arcsin(x, DEFAULT_TERMS), EPS);
    }

    @ParameterizedTest(name = "arcsin({0}) ≈ {1}")
    @DisplayName("Table values (negative)")
    @CsvSource({
            "-0.5,      -0.5235987755982988",
            "-0.7071067811865476, -0.7853981633974483",
            "-0.8660254037844386, -1.0471975511965976",
    })
    void testNegativeTableValues(double x, double expected) {
        assertEquals(expected, ArcSinSeries.arcsin(x, DEFAULT_TERMS), EPS);
    }

    @ParameterizedTest(name = "arcsin({0}) — oddness")
    @DisplayName("Odd function: arcsin(-x) = -arcsin(x)")
    @ValueSource(doubles = {0.1, 0.25, 0.4, 0.6, 0.75, 0.9})
    void testOddFunctionProperty(double x) {
        double pos = ArcSinSeries.arcsin(x, DEFAULT_TERMS);
        double neg = ArcSinSeries.arcsin(-x, DEFAULT_TERMS);
        assertEquals(-pos, neg, EPS);
    }

    @Test
    @DisplayName("Boundary value: arcsin(1) = π/2")
    void testBoundaryOne() {
        double piHalf = PI / 2.0;
        assertEquals(piHalf, ArcSinSeries.arcsin(1.0, 5000), 1e-2);
    }

    @Test
    @DisplayName("Boundary value: arcsin(-1) = -π/2")
    void testBoundaryMinusOne() {
        double minusPiHalf = -PI / 2.0;
        assertEquals(minusPiHalf, ArcSinSeries.arcsin(-1.0, 5000), 1e-2);
    }

    @ParameterizedTest(name = "arcsin({0}) ≈ {0} for small x")
    @DisplayName("Small x: arcsin(x) ≈ x")
    @ValueSource(doubles = {1e-8, 1e-6, 1e-4})
    void testSmallValues(double x) {
        assertEquals(x, ArcSinSeries.arcsin(x, 5), 1e-10);
    }

    @ParameterizedTest(name = "Convergence at x={0}")
    @DisplayName("More terms produce better accuracy")
    @ValueSource(doubles = {0.3, 0.6, 0.8, 0.95})
    void testConvergence(double x) {
        double lowAcc = ArcSinSeries.arcsin(x, 5);
        double highAcc = ArcSinSeries.arcsin(x, 50);
        double veryHighAcc = ArcSinSeries.arcsin(x, 200);

        assertTrue(
                java.lang.Math.abs(highAcc - veryHighAcc) < java.lang.Math.abs(lowAcc - veryHighAcc),
                "More terms should improve approximation accuracy"
        );
    }

    @ParameterizedTest(name = "arcsin({0}) — outside [-1,1]")
    @DisplayName("IllegalArgumentException for |x| > 1")
    @ValueSource(doubles = {1.1, -1.1, 2.0, -5.0, 100.0})
    void testOutOfRange(double x) {
        assertThrows(IllegalArgumentException.class,
                () -> ArcSinSeries.arcsin(x, 10));
    }

    @Test
    @DisplayName("IllegalArgumentException for terms <= 0")
    void testInvalidTerms() {
        assertThrows(IllegalArgumentException.class,
                () -> ArcSinSeries.arcsin(0.5, 0));
        assertThrows(IllegalArgumentException.class,
                () -> ArcSinSeries.arcsin(0.5, -1));
    }

    @ParameterizedTest(name = "arcsin({0}) — non-finite x")
    @DisplayName("IllegalArgumentException for NaN/Infinity")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testNonFiniteX(double x) {
        assertThrows(IllegalArgumentException.class,
                () -> ArcSinSeries.arcsin(x, 10));
    }

    @Test
    @DisplayName("Single term: arcsin(x) ≈ x")
    void testSingleTerm() {
        assertEquals(0.5, ArcSinSeries.arcsin(0.5, 1), EPS);
        assertEquals(0.0, ArcSinSeries.arcsin(0.0, 1), EPS);
    }
}