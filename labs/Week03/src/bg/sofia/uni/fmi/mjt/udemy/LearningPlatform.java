package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public interface LearningPlatform {

    /**
     * Returns the course with the given name.
     *
     * @param name the exact name of the course.
     * @throws IllegalArgumentException if name is null or blank.
     * @throws CourseNotFoundException if course with the specified name is not present in the platform.
     */
    Course findByName(String name) throws CourseNotFoundException;

    /**
     * Returns all courses which name or description containing keyword.
     * A keyword is a word that consists of only small and capital latin letters.
     *
     * @param keyword the exact keyword for which we will search.
     * @throws IllegalArgumentException if keyword is null, blank or not a keyword.
     */
    Course[] findByKeyword(String keyword);

    /**
     * Returns all courses from a given category.
     *
     * @param category the exact category the courses for which we want to get.
     * @throws IllegalArgumentException if category is null.
     */
    Course[] getAllCoursesByCategory(Category category);

    /**
     * Returns the account with the given name.
     *
     * @param name the exact name of the account.
     * @throws IllegalArgumentException if name is null or blank.
     * @throws AccountNotFoundException if account with such a name does not exist in the platform.
     */
    Account getAccount(String name) throws AccountNotFoundException;

    /**
     * Returns the course with the longest duration in the platform.
     * Returns null if the platform has no courses.
     */
    Course getLongestCourse();

    /**
     * Returns the cheapest course from the given category.
     * Returns null if the platform has no courses.
     *
     * @param category the exact category for which we want to find the cheapest course.
     * @throws IllegalArgumentException if category is null.
     */
    Course getCheapestByCategory(Category category);
}
