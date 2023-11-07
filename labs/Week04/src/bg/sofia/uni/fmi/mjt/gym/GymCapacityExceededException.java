package bg.sofia.uni.fmi.mjt.gym;

public class GymCapacityExceededException extends Exception {

    public GymCapacityExceededException() {
        super("Gym capacity has exceeded!");
    }

    public GymCapacityExceededException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public GymCapacityExceededException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
