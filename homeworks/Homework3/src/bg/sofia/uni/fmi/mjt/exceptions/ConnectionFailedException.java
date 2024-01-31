package bg.sofia.uni.fmi.mjt.exceptions;

public class ConnectionFailedException extends Exception {

    public ConnectionFailedException() {
        super("Connection was interrupted abruptly.");
    }

    public ConnectionFailedException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public ConnectionFailedException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
