package bg.sofia.uni.fmi.mjt.spotify.exception.authentication;

public class WrongPasswordException extends AuthenticationException {
    public WrongPasswordException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public WrongPasswordException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
