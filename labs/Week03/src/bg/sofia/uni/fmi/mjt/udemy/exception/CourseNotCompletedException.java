package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotCompletedException extends Exception {

    public CourseNotCompletedException() { super("Course not completed!"); }

    public CourseNotCompletedException(String errorMessage) { super(errorMessage); }

    public CourseNotCompletedException(String errorMessage, Throwable err) { super(errorMessage, err); }
}
