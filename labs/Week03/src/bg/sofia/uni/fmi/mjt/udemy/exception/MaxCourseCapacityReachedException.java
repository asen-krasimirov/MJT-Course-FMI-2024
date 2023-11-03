package bg.sofia.uni.fmi.mjt.udemy.exception;

public class MaxCourseCapacityReachedException extends Exception {

    public MaxCourseCapacityReachedException() { super("Max course capacity reached!"); }

    public MaxCourseCapacityReachedException(String errorMessage) { super(errorMessage); }

    public MaxCourseCapacityReachedException(String errorMessage, Throwable err) { super(errorMessage, err); }
}
