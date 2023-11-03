package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotFoundException extends Exception {

    public CourseNotFoundException() { super("Course has not found!"); }

    public CourseNotFoundException(String errorMessage) { super(errorMessage); }

    public CourseNotFoundException(String errorMessage, Throwable err) { super(errorMessage, err); }
}
