package bg.sofia.uni.fmi.mjt.spotify.exception.playlist;

public class PlaylistException extends Exception {
    public PlaylistException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public PlaylistException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
