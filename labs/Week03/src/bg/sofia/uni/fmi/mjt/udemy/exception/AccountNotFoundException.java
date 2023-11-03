package bg.sofia.uni.fmi.mjt.udemy.exception;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException() { super("Account has not found!"); }

    public AccountNotFoundException(String errorMessage) { super(errorMessage); }

    public AccountNotFoundException(String errorMessage, Throwable err) { super(errorMessage, err); }
}
