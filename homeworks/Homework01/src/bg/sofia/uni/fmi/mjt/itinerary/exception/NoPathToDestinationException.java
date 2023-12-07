package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class NoPathToDestinationException extends Exception {

    public NoPathToDestinationException() {
        super("There is no path satisfying the conditions!");
    }

    public NoPathToDestinationException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public NoPathToDestinationException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
