package bg.sofia.uni.fmi.mjt.spotify.logger;

import static bg.sofia.uni.fmi.mjt.spotify.config.SpotifyConfig.LOG_FILE_SOURCE;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

public class ExceptionLogger {
    public static void logException(Exception exception) {
        try (FileWriter logWriter = new FileWriter(LOG_FILE_SOURCE, true)) {
            logWriter
                .append(exception.getMessage())
                .append(System.lineSeparator())
                .append(Arrays.toString(exception.getStackTrace()))
                .append(System.lineSeparator());
        } catch (IOException ioException) {
            throw new UncheckedIOException("A problem occurred with writing to file.", ioException);
        }
    }

    public static void logException(Exception exception, String clientInput) {
        try (FileWriter logWriter = new FileWriter(LOG_FILE_SOURCE, true)) {
            logWriter
                .append(exception.getMessage())
                .append(System.lineSeparator())
                .append(Arrays.toString(exception.getStackTrace())).append(System.lineSeparator())
                .append("Client Input: ")
                .append(clientInput);
        } catch (IOException ioException) {
            throw new UncheckedIOException("A problem occurred with writing to file.", ioException);
        }
    }
}
