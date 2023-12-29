package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.MJTOrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TShirtShopServer {
    private static final int SERVER_PORT = 4444;
    private static final int MAX_EXECUTOR_THREADS = 10;
    private static final OrderRepository ORDER_REPOSITORY = new MJTOrderRepository();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            Socket clientSocket;

            while (true) {
                clientSocket = serverSocket.accept();

                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, ORDER_REPOSITORY);

                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }
}
