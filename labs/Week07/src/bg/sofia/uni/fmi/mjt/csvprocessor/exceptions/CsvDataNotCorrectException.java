package bg.sofia.uni.fmi.mjt.csvprocessor.exceptions;

public class CsvDataNotCorrectException extends Exception {

    CsvDataNotCorrectException() {
        super("Trying to parse wrong data!");
    }

    CsvDataNotCorrectException(String exceptionMessage) {
        super(exceptionMessage);
    }

    CsvDataNotCorrectException(String exceptionMessage, Throwable previousException) {
        super(exceptionMessage, previousException);
    }

}
