package bg.sofia.uni.fmi.mjt.spotify.exception.playlist;

public class SuchPlaylistAlreadyExistsException extends PlaylistException {
    public SuchPlaylistAlreadyExistsException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public SuchPlaylistAlreadyExistsException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
