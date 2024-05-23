package bg.sofia.uni.fmi.mjt.spotify.exception.database;

public class IncorrectDataFormatException extends RuntimeException {
    public IncorrectDataFormatException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public IncorrectDataFormatException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
