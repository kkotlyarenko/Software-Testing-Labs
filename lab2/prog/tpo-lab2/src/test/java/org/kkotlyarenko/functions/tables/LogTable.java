package org.kkotlyarenko.functions.tables;

import java.util.Map;

public class LogTable {
    public static final Map<Double, Double> LN = Map.ofEntries(
            Map.entry(1.0, 0.0),
            Map.entry(2.718281828459045, 1.0),         // e
            Map.entry(7.38905609893065, 2.0),          // e^2
            Map.entry(0.36787944117144233, -1.0),      // 1/e
            Map.entry(0.04, -3.2188758249),
            Map.entry(0.2, -1.6094379124341003),
            Map.entry(0.25, -1.3862943611198906),          // 1/e^2
            Map.entry(0.5, -0.6931471805599453),
            Map.entry(2.0, 0.6931471805599453),
            Map.entry(3.0, 1.0986122886681098),
            Map.entry(4.0, 1.3862943611198906),
            Map.entry(5.0, 1.6094379124341003),
            Map.entry(8.0, 2.079441541679836),
            Map.entry(10.0, 2.302585092994046),
            Map.entry(25.0, 3.2188758248682006),
            Map.entry(125.0, 4.8283137373023015),
            Map.entry(0.1, -2.302585092994046),
            Map.entry(0.01, -4.605170185988091),
            Map.entry(100.0, 4.605170185988091),
            Map.entry(1000.0, 6.907755278982137)
    );

    public static final Map<Double, Double> LOG2 = Map.ofEntries(
            Map.entry(1.0, 0.0),
            Map.entry(2.0, 1.0),                      // 2^1
            Map.entry(4.0, 2.0),                      // 2^2
            Map.entry(8.0, 3.0),                      // 2^3
            Map.entry(0.5, -1.0),                    // 2^-1
            Map.entry(0.25, -2.0),                   // 2^-2
            Map.entry(3.0, 1.5849625007211563),
            Map.entry(5.0, 2.321928094887362),
            Map.entry(10.0, 3.321928094887362)
    );

    public static final Map<Double, Double> LOG5 = Map.ofEntries(
            Map.entry(1.0, 0.0),
            Map.entry(5.0, 1.0),                      // 5^1
            Map.entry(25.0, 2.0),                     // 5^2
            Map.entry(125.0, 3.0),                    // 5^3
            Map.entry(0.2, -1.0),                    // 5^-1
            Map.entry(0.04, -2.0),                   // 5^-2
            Map.entry(2.0, 0.43067655807339306),
            Map.entry(10.0, 1.430676558073393),
            Map.entry(100.0, 2.861353116146786)
    );

    public static final Map<Double, Double> LOG10 = Map.ofEntries(
            Map.entry(1.0, 0.0),
            Map.entry(10.0, 1.0),                     // 10^1
            Map.entry(100.0, 2.0),                    // 10^2
            Map.entry(1000.0, 3.0),                   // 10^3
            Map.entry(0.1, -1.0),                    // 10^-1
            Map.entry(0.01, -2.0),                   // 10^-2
            Map.entry(2.0, 0.3010299956639812),
            Map.entry(3.0, 0.4771212547196624),
            Map.entry(5.0, 0.6989700043360189)
    );
}
