package bg.sofia.uni.fmi.mjt.itinerary;

import java.math.BigDecimal;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import static bg.sofia.uni.fmi.mjt.itinerary.utils.DistanceCalculator.calculateDistance;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price) implements Comparable<Journey> {

    public BigDecimal taxedPrice() {
        return price.add(price.multiply(vehicleType.getGreenTax()));
    }

    public BigDecimal getDistance() {
        return calculateDistance(from.location(), to.location());
    }

    @Override
    public int compareTo(Journey other) {
        return getDistance().compareTo(other.getDistance());
    }

}
