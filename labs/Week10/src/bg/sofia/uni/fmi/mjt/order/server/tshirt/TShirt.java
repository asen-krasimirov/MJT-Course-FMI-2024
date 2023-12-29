package bg.sofia.uni.fmi.mjt.order.server.tshirt;

import java.util.Objects;

public record TShirt(Size size, Color color) {
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof TShirt tshirt)) {
            return false;
        }

        return this.size().equals(tshirt.size()) && this.color().equals(tshirt.color());
    }

    @Override
    public int hashCode() {
        return Objects.hash(size().hashCode() + color.hashCode());
    }
}
