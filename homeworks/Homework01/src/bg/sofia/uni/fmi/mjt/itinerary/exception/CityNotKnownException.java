package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class CityNotKnownException extends Exception {

    public CityNotKnownException() {
        super("City is not present!");
    }

    public CityNotKnownException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public CityNotKnownException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
