package bg.sofia.uni.fmi.mjt.spotify.exception.authentication;

public class UserNotLoggedException extends AuthenticationException {
    public UserNotLoggedException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public UserNotLoggedException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
