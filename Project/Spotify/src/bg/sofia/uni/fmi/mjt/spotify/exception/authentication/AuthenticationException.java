package bg.sofia.uni.fmi.mjt.spotify.exception.authentication;

public class AuthenticationException extends Exception {
    public AuthenticationException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public AuthenticationException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
