package bg.sofia.uni.fmi.mjt.itinerary.vehicle;

import java.math.BigDecimal;

public enum VehicleType {

    PLANE(new BigDecimal("0.25")),
    TRAIN(new BigDecimal("0")),
    BUS(new BigDecimal("0.1"));

    private final BigDecimal greenTax;

    VehicleType(BigDecimal greenTax) {
        this.greenTax = greenTax;
    }

    public BigDecimal getGreenTax() {
        return greenTax;
    }

}
