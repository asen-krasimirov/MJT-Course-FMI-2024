package bg.sofia.uni.fmi.mjt.udemy.exception;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public ResourceNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
