package bg.sofia.uni.fmi.mjt.itinerary;

import java.util.Objects;

public record City(String name, Location location) {

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof City city)) {
            return false;
        }

        return Objects.equals(this.name(), city.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name());
    }

}
