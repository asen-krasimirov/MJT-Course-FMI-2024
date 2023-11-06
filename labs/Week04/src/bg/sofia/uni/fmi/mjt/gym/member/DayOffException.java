package bg.sofia.uni.fmi.mjt.gym.member;

public class DayOffException extends RuntimeException {

    public DayOffException() {
        super("Workout is null!");
    }

    public DayOffException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public DayOffException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
