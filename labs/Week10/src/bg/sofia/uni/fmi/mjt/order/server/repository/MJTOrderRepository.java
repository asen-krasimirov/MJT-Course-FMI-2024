package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MJTOrderRepository implements OrderRepository {
    private final List<Order> orders;
    private int successfulOrdersCount = 0;

    public MJTOrderRepository() {
        orders = new ArrayList<>();
    }

    @Override
    public Response request(String size, String color, String destination) {
        if (size == null || color == null || destination == null) {
            throw new IllegalArgumentException("Values of size, color and destination should not be null!");
        }

        Size sizeValue = Size.findByValue(size);
        Color colorValue = Color.findByValue(color);
        Destination destinationValue = Destination.findByValue(destination);

        boolean isSizeValid = !sizeValue.getName().equals("UNKNOWN");
        boolean isColorValid = !colorValue.getName().equals("UNKNOWN");
        boolean isDestinationValid = !destinationValue.getName().equals("UNKNOWN");

        if (isSizeValid && isColorValid && isDestinationValid) {
            addOrder(++successfulOrdersCount, sizeValue, colorValue, destinationValue);
            return Response.create(orders.size());
        }

        addOrder(-1, sizeValue, colorValue, destinationValue);

        StringBuilder errorMessages = getErrorMessages(isSizeValid, isColorValid, isDestinationValid);

        return Response.decline(errorMessages.toString());
    }

    private void addOrder(int id, Size sizeValue, Color colorValue, Destination destinationValue) {
        TShirt tshirt = new TShirt(sizeValue, colorValue);
        Order order = new Order(id, tshirt, destinationValue);
        orders.add(order);
    }

    private StringBuilder getErrorMessages(boolean isSizeValid, boolean isColorValid,
                                                  boolean isDestinationValid) {
        StringBuilder errorMessages = new StringBuilder("invalid:");

        if (!isSizeValid) {
            errorMessages.append("size");

            if (!isColorValid || !isDestinationValid) {
                errorMessages.append(",");
            }
        }

        if (!isColorValid) {
            errorMessages.append("color");

            if (!isDestinationValid) {
                errorMessages.append(",");
            }
        }

        if (!isDestinationValid) {
            errorMessages.append("destination");
        }
        return errorMessages;
    }

    @Override
    public Response getOrderById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Value of id should not be non-positive number!");
        }

        Optional<Order> order = orders
            .stream()
            .filter(o -> o.id() == id)
            .findFirst();

        if (order.isPresent()) {
            return Response.ok(List.of(order.get()));
        }

        return Response.notFound(id);
    }

    @Override
    public Response getAllOrders() {
        Collection<Order> returnOrders = List.copyOf(orders);
        return Response.ok(returnOrders);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        Collection<Order> successfulOrders = orders
            .stream()
            .filter(o -> o.id() != -1)
            .toList();

        return Response.ok(successfulOrders);
    }
}
