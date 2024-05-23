package bg.sofia.uni.fmi.mjt.spotify.server;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import bg.sofia.uni.fmi.mjt.spotify.Spotify;
import bg.sofia.uni.fmi.mjt.spotify.SpotifyAPI;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.spotify.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.spotify.logger.ExceptionLogger;

public class SpotifyServer {
    private static final String COMMAND_RECEIVED_MESSAGE = "Command \"%s\" received from client \"%s\".";

    private static final int USED_PORT = 6666;

    private static final String HOST = "localhost";

    private static final int BUFFER_SIZE = 1024;

    private final SpotifyAPI spotify;

    private final CommandExecutor commandExecutor;

    private final int port;

    private boolean isServerWorking;

    private ByteBuffer buffer;

    private Selector selector;

    public SpotifyServer(int port, SpotifyAPI spotify) {
        this.port = port;
        this.spotify = spotify;
        this.commandExecutor = new CommandExecutor(spotify);

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));

        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8).trim();
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put((output + System.lineSeparator()).getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey clientKey) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) clientKey.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    private void startConfigurations(ServerSocketChannel serverSocketChannel) throws IOException {
        spotify.loadDatabases();

        selector = Selector.open();
        configureServerSocketChannel(serverSocketChannel, selector);

        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);

        isServerWorking = true;
    }

    private void handleSelectedKeys() throws IOException {
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

        while (keyIterator.hasNext()) {
            SelectionKey clientKey = keyIterator.next();

            if (clientKey.isReadable()) {
                SocketChannel clientChannel = (SocketChannel) clientKey.channel();
                String clientInput = getClientInput(clientChannel);
                System.out.printf((COMMAND_RECEIVED_MESSAGE) + "%n", clientInput, clientChannel.getRemoteAddress());

                if (clientInput == null) {
                    continue;
                }

                String output = commandExecutor.execute(clientKey, CommandCreator.newCommand(clientInput));

                if (clientInput.equals("disconnect")) {
                    clientChannel.close();
                } else {
                    writeClientOutput(clientChannel, output);
                }
            } else if (clientKey.isAcceptable()) {
                accept(selector, clientKey);
            }

            keyIterator.remove();
        }
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            startConfigurations(serverSocketChannel);

            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    handleSelectedKeys();
                } catch (IOException exception) {
                    ExceptionLogger.logException(exception);
                    System.out.println("Error occurred while processing client request: " + exception.getMessage());
                }
            }
        } catch (IOException exception) {
            ExceptionLogger.logException(exception);
            throw new UncheckedIOException("Failed to start server.", exception);
        }
    }

    public void stop() {
        this.isServerWorking = false;

        if (selector.isOpen()) {
            selector.wakeup();
        }

        spotify.saveDatabases();
    }

    public static void main(String[] args) {
        SpotifyAPI spotify = new Spotify();

        SpotifyServer spotifyServer = new SpotifyServer(USED_PORT, spotify);

        spotifyServer.start();
    }
}
