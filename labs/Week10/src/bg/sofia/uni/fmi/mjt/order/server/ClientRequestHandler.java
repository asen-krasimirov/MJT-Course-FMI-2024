package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.repository.OrderRepository;
import bg.sofia.uni.fmi.mjt.order.server.utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {
    private final Socket socket;
    private final CommandHandler commandHandler;

    public ClientRequestHandler(Socket socket, OrderRepository orderRepository) {
        this.socket = socket;
        this.commandHandler = new CommandHandler(orderRepository);
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String response = commandHandler.handleCommand(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new UncheckedIOException("There was problem while working with the socket!", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new UncheckedIOException("There was problem while closing the socket!", e);
            }
        }
    }
}
