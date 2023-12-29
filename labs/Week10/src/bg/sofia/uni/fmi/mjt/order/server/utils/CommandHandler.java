package bg.sofia.uni.fmi.mjt.order.server.utils;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

public class CommandHandler {
    private final OrderRepository orderRepository;

    public CommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String handleCommand(String command) {
        String[] tokens = command.split(" ");
        String task = tokens[Index.INDEX_ZERO];

        if (task.equals("request")) {
            return handleRequest(tokens).toString();
        } else if (task.equals("get")) {
            String subTask = tokens[1];
            return handleGet(subTask, tokens).toString();
        } else {
            return "Unknown command";
        }

    }

    private Response handleRequest(String[] tokens) {
        String size = tokens[Index.INDEX_ONE].split("=")[Index.INDEX_ONE];
        String color = tokens[Index.INDEX_TWO].split("=")[Index.INDEX_ONE];
        String destination = tokens[Index.INDEX_THREE].split("=")[Index.INDEX_ONE];

        return orderRepository.request(size, color, destination);
    }

    private Response handleGet(String subTask, String[] tokens) {
        if (subTask.equals("all")) {
            return orderRepository.getAllOrders();
        } else if (subTask.equals("all-successful")) {
            return orderRepository.getAllSuccessfulOrders();
        } else {
            int id = Integer.parseInt(tokens[Index.INDEX_TWO].split("=")[Index.INDEX_ONE]);
            return orderRepository.getOrderById(id);
        }
    }
}
