package bg.sofia.uni.fmi.mjt.spotify.exception.song;

public class SongException extends Exception {
    public SongException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public SongException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
