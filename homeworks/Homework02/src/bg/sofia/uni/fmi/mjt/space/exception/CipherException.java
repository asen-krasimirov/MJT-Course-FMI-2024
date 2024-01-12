package bg.sofia.uni.fmi.mjt.space.exception;

public class CipherException extends Exception {
    public CipherException() {
        super("Problem occurred with the cipher! Encrypt/decrypt operation can not be completed successfully!");
    }

    public CipherException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public CipherException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }
}
