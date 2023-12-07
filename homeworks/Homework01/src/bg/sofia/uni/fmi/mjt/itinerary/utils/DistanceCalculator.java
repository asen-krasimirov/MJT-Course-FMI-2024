package bg.sofia.uni.fmi.mjt.itinerary.utils;

import bg.sofia.uni.fmi.mjt.itinerary.Location;

import java.math.BigDecimal;

import static java.lang.Math.abs;

public class DistanceCalculator {

    public static BigDecimal calculateManhattanDistance(Location location1, Location location2) {
        return BigDecimal.valueOf(
            abs(location1.x() - location2.x()) + abs(location1.y() - location2.y())
        );
    }

    public static BigDecimal calculateDistance(Location location1, Location location2) {
        int xDiff = location2.x() - location1.x();
        int yDiff = location2.y() - location1.y();

        return BigDecimal.valueOf(
            Math.sqrt(
                Math.pow(xDiff, 2) + Math.pow(yDiff, 2)
            )
        );
    }

}
