package bg.sofia.uni.fmi.mjt.spotify.exception.authentication;

public class WrongEmailFormatException extends AuthenticationException {
    public WrongEmailFormatException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public WrongEmailFormatException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
