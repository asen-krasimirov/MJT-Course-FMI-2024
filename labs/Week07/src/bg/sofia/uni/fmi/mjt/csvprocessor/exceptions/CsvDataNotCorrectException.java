package bg.sofia.uni.fmi.mjt.csvprocessor.exceptions;

public class CsvDataNotCorrectException extends Exception {

    public CsvDataNotCorrectException() {
        super("Trying to parse wrong data!");
    }

    public CsvDataNotCorrectException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public CsvDataNotCorrectException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
