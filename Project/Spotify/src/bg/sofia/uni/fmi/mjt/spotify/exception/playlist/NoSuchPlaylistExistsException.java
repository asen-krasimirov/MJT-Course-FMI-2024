package bg.sofia.uni.fmi.mjt.spotify.exception.playlist;

public class NoSuchPlaylistExistsException extends PlaylistException {
    public NoSuchPlaylistExistsException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public NoSuchPlaylistExistsException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
