package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseAlreadyPurchasedException extends Exception {

    public CourseAlreadyPurchasedException() { super("Course already purchased!"); }

    public CourseAlreadyPurchasedException(String errorMessage) { super(errorMessage); }

    public CourseAlreadyPurchasedException(String errorMessage, Throwable err) { super(errorMessage, err); }
}
