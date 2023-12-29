package bg.sofia.uni.fmi.mjt.order.server.order;

import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.Objects;

public record Order(int id, TShirt tShirt, Destination destination) {
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Order order)) {
            return false;
        }

        return this.id() == order.id() &&
            this.tShirt().equals(order.tShirt()) &&
            this.destination().equals(order.destination());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
