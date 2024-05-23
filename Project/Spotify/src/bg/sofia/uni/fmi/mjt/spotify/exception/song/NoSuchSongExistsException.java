package bg.sofia.uni.fmi.mjt.spotify.exception.song;

public class NoSuchSongExistsException extends SongException {
    public NoSuchSongExistsException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public NoSuchSongExistsException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
