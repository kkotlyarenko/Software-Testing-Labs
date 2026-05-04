package org.kkotlyarenko.functions.stubs;

import org.kkotlyarenko.functions.config.TestConfig;

import static org.mockito.Mockito.*;

import java.util.Map;

public class StubFactory {
    public static <T> T createStub(Class<T> clazz, Map<Double, Double> table) {
        T mock = mock(clazz);

        try {
            when(clazz.getMethod("calculate", double.class, double.class)
                    .invoke(mock, anyDouble(), anyDouble()))
                    .thenAnswer(inv -> {
                        double x = inv.getArgument(0);
                        for (Map.Entry<Double, Double> entry : table.entrySet()) {
                            if (Math.abs(entry.getKey() - x) < TestConfig.EPSILON) {
                                return entry.getValue();
                            }
                        }
                        return 0.0;
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mock;
    }

    public static <T> T createNaNStub(Class<T> clazz) {
        T mock = mock(clazz);

        try {
            when(clazz.getMethod("calculate", double.class, double.class)
                    .invoke(mock, anyDouble(), anyDouble()))
                    .thenReturn(Double.NaN);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mock;
    }
}
