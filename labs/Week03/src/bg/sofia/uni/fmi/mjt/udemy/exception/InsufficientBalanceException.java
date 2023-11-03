package bg.sofia.uni.fmi.mjt.udemy.exception;

public class InsufficientBalanceException extends Exception {

    public InsufficientBalanceException() {
        super("Insufficient balance!");
    }

    public InsufficientBalanceException(String errorMessage) {
        super(errorMessage);
    }

    public InsufficientBalanceException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
