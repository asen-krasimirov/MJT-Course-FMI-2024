package bg.sofia.uni.fmi.mjt.spotify.exception.playlist;

public class NoAccessToPlaylistException extends PlaylistException {
    public NoAccessToPlaylistException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public NoAccessToPlaylistException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
