package bg.sofia.uni.fmi.mjt.order.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TShirtShopClient {
    private static final int SERVER_PORT = 4444;

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();

                if ("disconnect".equals(message)) {
                    break;
                }

                writer.println(message);

                String response = reader.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
