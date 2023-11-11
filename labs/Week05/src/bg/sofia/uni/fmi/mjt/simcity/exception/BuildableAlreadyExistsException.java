package bg.sofia.uni.fmi.mjt.simcity.exception;

public class BuildableAlreadyExistsException extends RuntimeException {

    public BuildableAlreadyExistsException() {
        super("Buildable already created!");
    }

    public BuildableAlreadyExistsException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public BuildableAlreadyExistsException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
