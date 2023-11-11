package bg.sofia.uni.fmi.mjt.simcity.exception;

public class BuildableNotFoundException extends RuntimeException {

    public BuildableNotFoundException() {
        super("Buildable was not found!");
    }

    public BuildableNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public BuildableNotFoundException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
