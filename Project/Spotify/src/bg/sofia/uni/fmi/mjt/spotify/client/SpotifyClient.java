package bg.sofia.uni.fmi.mjt.spotify.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SpotifyClient {
    private static final int SERVER_PORT = 6666;

    private static final String HOST = "localhost";

    private static final String COMMAND_OPTIONS = """
        Choose one of the following options:
        -> register <email> <password>
        -> login <email> <password>
        -> disconnect
        -> search <words>
        -> top <number>
        -> create-playlist <name_of_the_playlist>
        -> add-song-to <name_of_the_playlist> <song>
        -> show-playlist <name_of_the_playlist>
        -> play <song>
        -> stop
        -> command-options""";

    private static final String PLAY_SONG_COMMAND = "play";

    private static final String STOP_SONG_COMMAND = "stop";

    private static final String DISCONNECT_COMMAND = "disconnect";

    private static final String COMMAND_OPTIONS_COMMAND = "command-options";

    private static SongReceiveThread songReceiveThread = null;

    private static void displayCommandOptions() {
        System.out.println(COMMAND_OPTIONS);
    }

    private static void initialiseConnection(SocketChannel socketChannel) throws IOException {
        socketChannel.connect(new InetSocketAddress(HOST, SERVER_PORT));

        System.out.println("Connected to the server.");

        songReceiveThread = null;
    }

    private static void handlePlaySong(PrintWriter writer, String command, BufferedReader reader,
                                       SocketChannel socketChannel) throws IOException {
        writer.println(command);
        String response = reader.readLine();

        if (response.startsWith("Error")) {
            System.out.println(response);
            return;
        }

        songReceiveThread = new SongReceiveThread(socketChannel);
        songReceiveThread.start();
        System.out.println(response);
    }

    private static void handleStopSong(PrintWriter writer, String command) throws InterruptedException {
        if (songReceiveThread != null && songReceiveThread.isAlive()) {
            writer.println(command);
            songReceiveThread.join();
            System.out.println("Song stopped.");
        } else {
            System.out.println("No song is currently playing.");
        }
    }

    private static boolean handleCommand(PrintWriter writer, String command, BufferedReader reader) throws IOException {
        writer.println(command);

        if (command.equals(DISCONNECT_COMMAND)) {
            return false;
        }

        System.out.println(reader.readLine());

        return true;
    }

    private static void communicationLoop(Scanner scanner, PrintWriter writer, BufferedReader reader,
                                          SocketChannel socketChannel)
        throws IOException, InterruptedException {
        displayCommandOptions();

        while (true) {

            System.out.print("Enter message: ");

            String command = scanner.nextLine();

            if (command.equals(COMMAND_OPTIONS_COMMAND)) {
                displayCommandOptions();
                continue;
            }

            if (!command.startsWith(STOP_SONG_COMMAND) && songReceiveThread != null && songReceiveThread.isAlive()) {
                System.out.println("You should stop the song being played first.");
                continue;
            }

            if (command.startsWith(PLAY_SONG_COMMAND)) {
                handlePlaySong(writer, command, reader, socketChannel);
            } else if (command.startsWith(STOP_SONG_COMMAND)) {
                handleStopSong(writer, command);
            } else {
                if (!handleCommand(writer, command, reader)) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            initialiseConnection(socketChannel);

            communicationLoop(scanner, writer, reader, socketChannel);
        } catch (IOException exception) {
            throw new RuntimeException("There is a problem with the network communication", exception);
        } catch (InterruptedException exception) {
            throw new RuntimeException("Thread was unexpectedly.", exception);
        }
    }
}
