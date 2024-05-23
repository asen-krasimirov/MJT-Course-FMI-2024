package bg.sofia.uni.fmi.mjt.spotify.song;

import bg.sofia.uni.fmi.mjt.spotify.logger.ExceptionLogger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SongStreamThread extends Thread {
    private static final int BUFFER_SIZE = 4096;

    private static final String END_FLAG = "ENDED";

    private static final int LAG_PREVENTION = 10;

    private final SelectionKey clientKey;

    private final Song song;

    private boolean isPlaying = false;

    public SongStreamThread(SelectionKey clientKey, Song song) {
        this.clientKey = clientKey;
        this.song = song;
    }

    private void sendFormat(AudioFormat format, ByteBuffer buffer, SocketChannel socketChannel) throws IOException {
        String formatToSend = format.getEncoding().toString() + System.lineSeparator() +
                              format.getSampleRate() + System.lineSeparator() +
                              format.getSampleSizeInBits() + System.lineSeparator() +
                              format.getChannels() + System.lineSeparator() +
                              format.getFrameSize() + System.lineSeparator() +
                              format.getFrameRate() + System.lineSeparator() +
                              format.isBigEndian();

        buffer.clear();
        buffer.put(formatToSend.getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        socketChannel.write(buffer);

        buffer.clear();
        buffer.put(formatToSend.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
    }

    private void steamingLoop(ByteBuffer buffer, AudioInputStream stream, SocketChannel socketChannel)
        throws IOException, InterruptedException {
        byte[] byteBuffer = new byte[BUFFER_SIZE];

        while (isPlaying && stream.read(byteBuffer) != -1) {
            buffer.clear();
            buffer.put(byteBuffer);
            buffer.flip();

            socketChannel.write(buffer);
            Thread.sleep(LAG_PREVENTION);
        }
    }

    private void sendEndFlag(ByteBuffer buffer, SocketChannel socketChannel) throws IOException {
        buffer.clear();
        buffer.put(END_FLAG.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socketChannel.write(buffer);
    }

    @Override
    public void run() {
        try {
            isPlaying = true;
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(song.getSongPath()));
            AudioFormat format = stream.getFormat();
            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            SocketChannel socketChannel = (SocketChannel) clientKey.channel();

            sendFormat(format, buffer, socketChannel);

            steamingLoop(buffer, stream, socketChannel);

            sendEndFlag(buffer, socketChannel);

            stream.close();
        } catch (IOException exception) {
            ExceptionLogger.logException(exception);
            throw new UncheckedIOException("A problem occurred with reading from file.", exception);
        } catch (UnsupportedAudioFileException exception) {
            ExceptionLogger.logException(exception);
            throw new RuntimeException("Unsupported audio file in server database.", exception);
        } catch (InterruptedException exception) {
            ExceptionLogger.logException(exception);
            throw new RuntimeException("Unexpected interruption of thread", exception);
        }
    }

    public void stopSong() {
        isPlaying = false;
    }
}
