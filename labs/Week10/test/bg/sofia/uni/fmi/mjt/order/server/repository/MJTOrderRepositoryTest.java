package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MJTOrderRepositoryTest {
    private OrderRepository orderRepository;
    private static List<Order> expectedOrders;

    @BeforeAll
    static void setUpTestCase() {
        TShirt expectedTShirt1 = new TShirt(Size.L, Color.BLACK);
        Order expectedOrder1 = new Order(1, expectedTShirt1, Destination.EUROPE);

        TShirt expectedTShirt2 = new TShirt(Size.M, Color.RED);
        Order expectedOrder2 = new Order(2, expectedTShirt2, Destination.AUSTRALIA);

        TShirt expectedTShirt3 = new TShirt(Size.UNKNOWN, Color.UNKNOWN);
        Order expectedOrder3 = new Order(-1, expectedTShirt3, Destination.UNKNOWN);

        expectedOrders = List.of(expectedOrder1, expectedOrder2, expectedOrder3);
    }

    @BeforeEach
    void setUp() {
        this.orderRepository = new MJTOrderRepository();
    }

    @Test
    void testRequestWithValidArguments() {
        Response expectedResult = Response.create(1);

        Response result = orderRepository.request("L", "BLACK", "EUROPE");

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both CREATED.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both \"ORDER_ID=1\".");
        Assertions.assertEquals(expectedResult.orders(), result.orders(), "Orders values should be both null.");
    }

    @Test
    void testRequestWithInvalidSize() {
        Response expectedResult = Response.decline("invalid:size");

        Response result = orderRepository.request("K", "BLACK", "EUROPE");

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both DECLINED.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both \"invalid:size\".");
        Assertions.assertEquals(expectedResult.orders(), result.orders(), "Orders values should be both null.");
    }

    @Test
    void testRequestWithNullSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderRepository.request(null, "BLACK", "EUROPE"),
            "request(...) should throw IllegalArgumentException when called with null value of size.");
    }

    @Test
    void testRequestWithInvalidColor() {
        Response expectedResult = Response.decline("invalid:color");

        Response result = orderRepository.request("L", "ORANGE", "EUROPE");

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both DECLINED.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both \"invalid:color\".");
        Assertions.assertEquals(expectedResult.orders(), result.orders(), "Orders values should be both null.");
    }

    @Test
    void testRequestWithNullColor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderRepository.request("L", null, "EUROPE"),
            "request(...) should throw IllegalArgumentException when called with null value of color.");
    }

    @Test
    void testRequestWithInvalidDestination() {
        Response expectedResult = Response.decline("invalid:destination");

        Response result = orderRepository.request("L", "BLACK", "NARNIA");

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both DECLINED.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both \"invalid:destination\".");
        Assertions.assertEquals(expectedResult.orders(), result.orders(), "Orders values should be both null.");
    }

    @Test
    void testRequestWithNullDestination() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderRepository.request("L", "BLACK", null),
            "request(...) should throw IllegalArgumentException when called with null value of destination.");
    }

    @Test
    void testRequestWithMultipleInvalidArguments() {
        Response expectedResult = Response.decline("invalid:size,color,destination");

        Response result = orderRepository.request("K", "ORANGE", "NARNIA");

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both DECLINED.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both \"invalid:size,color,destination\".");
        Assertions.assertEquals(expectedResult.orders(), result.orders(), "Orders values should be both null.");
    }

    @Test
    void testGetOrderByIdWithCorrectId() {
        Response expectedResult = Response.ok(List.of(expectedOrders.get(1)));

        orderRepository.request("L", "BLACK", "EUROPE");
        orderRepository.request("M", "RED", "AUSTRALIA");

        Response result = orderRepository.getOrderById(2);

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both OK.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both null.");
        Assertions.assertEquals(expectedResult.orders(), result.orders(),
            "Orders values should be both List with the correct order.");
    }

    @Test
    void testGetOrderByIdWithInvalidId() {
        Response expectedResult = Response.notFound(1);

        Response result = orderRepository.getOrderById(1);

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both NOT_FOUND.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both \"Order with id = 1 does not exist.\".");
        Assertions.assertEquals(expectedResult.orders(), result.orders(), "Orders values should be both null.");
    }

    @Test
    void testGetOrderByIdWithNonPositiveId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderRepository.getOrderById(-1),
            "getOrderById(...) should throw IllegalArgumentException when called with non-positive number.");
    }

    @Test
    void testGetAllOrdersWithFilledDataset() {
        Response expectedResult = Response.ok(expectedOrders);

        orderRepository.request("L", "BLACK", "EUROPE");
        orderRepository.request("M", "RED", "AUSTRALIA");
        orderRepository.request("K", "ORANGE", "NARNIA");

        Response result = orderRepository.getAllOrders();

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both OK.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both null.");
        Assertions.assertEquals(expectedResult.orders(), result.orders(),
            "Orders values should be both List with the correct orders.");
    }

    @Test
    void testGetAllOrdersWithEmptyDataset() {
        Response expectedResult = Response.ok(List.of());

        Response result = orderRepository.getAllOrders();

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both OK.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both null.");
        Assertions.assertEquals(expectedResult.orders(), result.orders(), "Orders values should be both empty List.");
    }

    @Test
    void testGetAllSuccessfulOrdersWithFilledDataset() {
        Response expectedResult = Response.ok(List.of(expectedOrders.get(0), expectedOrders.get(1)));

        orderRepository.request("L", "BLACK", "EUROPE");
        orderRepository.request("M", "RED", "AUSTRALIA");
        orderRepository.request("K", "ORANGE", "NARNIA");

        Response result = orderRepository.getAllSuccessfulOrders();

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both OK.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both null.");
        Assertions.assertEquals(expectedResult.orders(), result.orders(),
            "Orders values should be both List with the correct orders.");
    }

    @Test
    void testGetAllSuccessfulOrdersWithEmptyDataset() {
        Response expectedResult = Response.ok(List.of());

        Response result = orderRepository.getAllSuccessfulOrders();

        Assertions.assertEquals(expectedResult.status(), result.status(), "Statuses should be both OK.");
        Assertions.assertEquals(expectedResult.additionalInfo(), result.additionalInfo(),
            "Additional info values should be both null.");
        Assertions.assertEquals(expectedResult.orders(), result.orders(), "Orders values should be both empty List.");
    }

}
