package bg.sofia.uni.fmi.mjt.spotify.exception.authentication;

public class UserAlreadyRegisteredException extends AuthenticationException {
    public UserAlreadyRegisteredException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public UserAlreadyRegisteredException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
