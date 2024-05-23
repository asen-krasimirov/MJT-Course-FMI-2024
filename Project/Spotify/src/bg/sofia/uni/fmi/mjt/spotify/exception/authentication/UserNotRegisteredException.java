package bg.sofia.uni.fmi.mjt.spotify.exception.authentication;

public class UserNotRegisteredException extends AuthenticationException {
    public UserNotRegisteredException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public UserNotRegisteredException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
