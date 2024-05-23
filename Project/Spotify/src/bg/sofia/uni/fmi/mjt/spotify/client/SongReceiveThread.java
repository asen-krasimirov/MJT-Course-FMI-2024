package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SongReceiveThread extends Thread {
    private static final int BUFFER_SIZE = 4096;

    private static final String END_FLAG = "ENDED";

    private final SocketChannel serverChannel;

    private AudioFormat audioFormat;

    private int bytesRead;

    private final byte[] byteBuffer;

    ByteBuffer buffer;

    public SongReceiveThread(SocketChannel serverChannel) throws IOException {
        this.serverChannel = serverChannel;

        byteBuffer = new byte[BUFFER_SIZE];

        buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        getAudioFormat();
    }

    private void getAudioFormat() throws IOException {
        bytesRead = serverChannel.read(buffer);
        byte[] destination = new byte[bytesRead];
        buffer.flip();
        buffer.get(destination, 0, bytesRead);

        String contents = new String(destination);

        String[] tokens = contents.split(System.lineSeparator());

        AudioFormat.Encoding encoding = new AudioFormat.Encoding(tokens[TokenIndex.INDEX_ZERO.getIndex()]);
        float sampleRate = Float.parseFloat(tokens[TokenIndex.INDEX_ONE.getIndex()]);
        int sampleSizeInBits = Integer.parseInt(tokens[TokenIndex.INDEX_TWO.getIndex()]);
        int channels = Integer.parseInt(tokens[TokenIndex.INDEX_THREE.getIndex()]);
        int frameSize = Integer.parseInt(tokens[TokenIndex.INDEX_FOUR.getIndex()]);
        float frameRate = Float.parseFloat(tokens[TokenIndex.INDEX_FIVE.getIndex()]);
        boolean bigEndian = Boolean.parseBoolean(tokens[TokenIndex.INDEX_SIX.getIndex()]);

        audioFormat =
            new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
    }

    @Override
    public void run() {
        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

            audioLine.open(audioFormat);
            audioLine.start();

            String response;

            while ((bytesRead = serverChannel.read(buffer)) != -1) {
                buffer.flip();
                buffer.get(byteBuffer, 0, bytesRead);

                response = new String(byteBuffer);

                if (response.contains(END_FLAG)) {
                    break;
                }

                audioLine.write(byteBuffer, 0, bytesRead);
                buffer.clear();
            }

            audioLine.drain();
            audioLine.close();
        } catch (IOException | LineUnavailableException exception) {
            throw new RuntimeException("Unexpected interruption of thread.", exception);
        }
    }
}
