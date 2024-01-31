package bg.sofia.uni.fmi.mjt.exceptions;

public class BadRequestException extends Exception {

    public BadRequestException() {
        super("Request with code 400 was returned- Bad Request.");
    }

    public BadRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public BadRequestException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
