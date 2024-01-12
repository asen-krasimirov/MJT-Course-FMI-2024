package bg.sofia.uni.fmi.mjt.space.exception;

public class TimeFrameMismatchException extends RuntimeException {
    public TimeFrameMismatchException() {
        super("There was a mismatch with the time frame!");
    }

    public TimeFrameMismatchException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public TimeFrameMismatchException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
