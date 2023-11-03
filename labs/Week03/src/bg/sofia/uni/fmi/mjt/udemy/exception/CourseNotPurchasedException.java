package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotPurchasedException extends Exception {

    public CourseNotPurchasedException() {
        super("Course not purchased!");
    }

    public CourseNotPurchasedException(String errorMessage) {
        super(errorMessage);
    }

    public CourseNotPurchasedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
