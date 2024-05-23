package bg.sofia.uni.fmi.mjt.spotify.exception.song;

public class NoSongCurrentlyStreamedException extends SongException {
    public NoSongCurrentlyStreamedException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public NoSongCurrentlyStreamedException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
