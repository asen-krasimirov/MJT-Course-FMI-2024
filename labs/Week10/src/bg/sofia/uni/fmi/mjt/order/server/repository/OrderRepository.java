package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;

public interface OrderRepository {
    /**
     * Creates a new T-Shirt order
     *
     * @param size        - size of the requested T-Shirt
     * @param color       - color of the requested T-Shirt
     * @param destination - destination of the requested T-Shirt
     * @throws IllegalArgumentException if either size, color or destination is null
     *
     * @return response which contains status and additional info (orderId or invalid parameters if there are such)
     */
    Response request(String size, String color, String destination);

    /**
     * Retrieves a T-Shirt order with the given id
     *
     * @param id order id
     * @return response which contains status and an order with the given id
     * @throws IllegalArgumentException if id is a non-positive number
     */
    Response getOrderById(int id);

    /**
     * Retrieves all T-Shirt orders
     *
     * @return response which contains status and  all T-Shirt orders from the repository, in undefined order.
     * If there are no orders in the repository, returns an empty collection.
     */
    Response getAllOrders();

    /**
     * Retrieves all successful orders for T-Shirts
     *
     * @return response which contains status and all successful orders for
     * T-Shirts from the repository, in undefined order.
     * If there are no such orders in the repository, returns an empty collection.
     */
    Response getAllSuccessfulOrders();
}
