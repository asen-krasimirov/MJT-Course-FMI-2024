package bg.sofia.uni.fmi.mjt.order.server.utils;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CommandHandlerTest {
    private CommandHandler commandHandler;
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
        commandHandler = new CommandHandler(new MJTOrderRepository());
    }

    @Test
    void testHandleRequestCommand() {
        String expectedResult = Response.create(1).toString();

        String result = commandHandler.handleCommand("request size=L color=BLACK destination=EUROPE");

        Assertions.assertEquals(expectedResult, result, "Both String values should be equal.");
    }

    @Test
    void testHandleGetAllOrdersCommand() {
        String expectedResult = Response.ok(expectedOrders).toString();

        commandHandler.handleCommand("request size=L color=BLACK destination=EUROPE");
        commandHandler.handleCommand("request size=M color=RED destination=AUSTRALIA");
        commandHandler.handleCommand("request size=K color=ORANGE destination=NARNIA");

        String result = commandHandler.handleCommand("get all");

        Assertions.assertEquals(expectedResult, result, "Both String values should be equal.");
    }

    @Test
    void testHandleGetAllSuccessfulOrdersCommand() {
        String expectedResult = Response.ok(List.of(expectedOrders.get(0), expectedOrders.get(1))).toString();

        commandHandler.handleCommand("request size=L color=BLACK destination=EUROPE");
        commandHandler.handleCommand("request size=M color=RED destination=AUSTRALIA");
        commandHandler.handleCommand("request size=K color=ORANGE destination=NARNIA");

        String result = commandHandler.handleCommand("get all-successful");

        Assertions.assertEquals(expectedResult, result, "Both String values should be equal.");
    }

    @Test
    void testHandleGetOrderByIdCommand() {
        String expectedResult = Response.ok(List.of(expectedOrders.get(0))).toString();

        commandHandler.handleCommand("request size=L color=BLACK destination=EUROPE");
        commandHandler.handleCommand("request size=M color=RED destination=AUSTRALIA");
        commandHandler.handleCommand("request size=K color=ORANGE destination=NARNIA");

        String result = commandHandler.handleCommand("get my-order id=1");

        Assertions.assertEquals(expectedResult, result, "Both String values should be equal.");
    }

    @Test
    void testHandleRandomCommand() {
        String expectedResult = "Unknown command";

        String result = commandHandler.handleCommand("hello");

        Assertions.assertEquals(expectedResult, result, "Both String values should be equal.");
    }
}
