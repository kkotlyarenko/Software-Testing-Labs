package org.kkotlyarenko.functions.tables.system;

import java.util.Map;

public class SystemTable {
    public static final Map<Double, Double> LOG = Map.ofEntries(
            Map.entry(0.5, -24.68425),            // random point
            Map.entry(1.0, Double.NaN),              // asymptote
            Map.entry(1.1, 196.44976),           // random point
            Map.entry(2.0, 28.77792),               // random point
            Map.entry(8.0, 10.9572),                // random point
            Map.entry(3000.0, 4.36107)              // random point

    );

    public static final Map<Double, Double> TRIG = Map.ofEntries(
            Map.entry(0.0, Double.NaN),               // asymptote
            Map.entry(-0.05, -400.43347),          // point between asymptote and extremum
            Map.entry(-0.47269, -6.65154),         // local extremum
            Map.entry(-0.81, -389.6303),           // point between extremum and asymptote
            Map.entry(-2.29, 357.5685),            // point between asymptote and zero
            Map.entry(-2.55257, 0.0),              // zero crossing point
            Map.entry(-3.05, -119.71755),           // point between zero and asymptote
            Map.entry(-3.141592653589793, Double.NaN), // asymptote
            Map.entry(-3.25, -85.20861),           // point asymptote and zero
            Map.entry(-3.65219, 0.0),              // zero crossing point
            Map.entry(-3.85, 233.85923),           // point between zero and asymptote
            Map.entry(-3.94, -1200.84933),          // point between asymptote and extremum
            Map.entry(-3.98271, -924.44025),       // local extremum
            Map.entry(-4.05, -1218.67888),         // point between extremum and asymptote
            Map.entry(-5.5, 203.2806),             // point between asymptote and extremum
            Map.entry(-5.71718, 24.38425),         // local extremum
            Map.entry(-5.77, 132.43818),            // point between extremum and asymptote
            Map.entry(-5.776, -414.51918),          // point between asymptote and extremum
            Map.entry(-5.87213, -6.54679),         // local extremum
            Map.entry(-6.23, -353.74938),          // point between extremum and asymptote
            Map.entry(-6.283185307179586, Double.NaN)  // asymptote
    );
}
