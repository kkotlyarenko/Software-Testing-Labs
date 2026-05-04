package org.kkotlyarenko.functions.tables.system;

import java.util.Map;

public class LogSystemTable {
    public static final Map<Double, Double> LN = Map.ofEntries(
            Map.entry(0.5, -0.6931471805599453),
            Map.entry(1.0, 0.0),
            Map.entry(1.1, 0.0953101798),
            Map.entry(2.0, 0.6931471805599453),
            Map.entry(8.0, 2.079441541679836),
            Map.entry(3000.0, 8.006367567)
    );

    public static final Map<Double, Double> LOG2 = Map.ofEntries(
            Map.entry(0.5, -1.0),
            Map.entry(1.0, 0.0),
            Map.entry(1.1, 0.1375035238),
            Map.entry(2.0, 1.0),
            Map.entry(8.0, 3.0),
            Map.entry(3000.0, 11.5507467854)
    );

    public static final Map<Double, Double> LOG5 = Map.ofEntries(
            Map.entry(0.5, -0.43067655807339306),
            Map.entry(1.0, 0.0),
            Map.entry(1.1, 0.05921954433),
            Map.entry(2.0, 0.43067655807339306),
            Map.entry(8.0, 1.292029674),
            Map.entry(3000.0, 4.9746358687)
    );

    public static final Map<Double, Double> LOG10 = Map.ofEntries(
            Map.entry(0.5, -0.3010299956639812),
            Map.entry(1.0, 0.0),
            Map.entry(1.1, 0.0413926852),
            Map.entry(2.0, 0.3010299956639812),
            Map.entry(8.0, 0.903089987),
            Map.entry(3000.0, 3.4771212547)
    );
}
