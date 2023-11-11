package bg.sofia.uni.fmi.mjt.simcity.exception;

public class InsufficientPlotAreaException extends RuntimeException {

    public InsufficientPlotAreaException() {
        super("Insufficient plot area!");
    }

    public InsufficientPlotAreaException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public InsufficientPlotAreaException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
