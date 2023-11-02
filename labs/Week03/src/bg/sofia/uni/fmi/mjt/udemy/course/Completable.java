package bg.sofia.uni.fmi.mjt.udemy.course;

public interface Completable {
    /**
     * Returns true if and only if the course is completed.
     */
    boolean isCompleted();

    /**
     * Returns the completion percentage of the resource.
     * The percentage is an integer in the range [0, 100].
     */
    int getCompletionPercentage();
}
