package bg.sofia.uni.fmi.mjt.exceptions;

public class ForbiddenAccessException extends Exception {

    public ForbiddenAccessException() {
        super("Request with code 403 was returned- Forbidden Access.");
    }

    public ForbiddenAccessException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public ForbiddenAccessException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
