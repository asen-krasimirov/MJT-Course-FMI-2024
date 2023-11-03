package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public interface Account {

    /**
     * Returns the username of the account.
     */
    String getUsername();

    /**
     * Adds the given amount of money to the account's balance.
     *
     * @param amount the amount of money which will be added to the account's balance.
     * @throws IllegalArgumentException if amount has a negative value.
     */
    void addToBalance(double amount);

    /**
     * Returns the balance of the account.
     */
    double getBalance();

    /**
     * Buys the given course for the account.
     *
     * @param course the course which will be bought.
     * @throws IllegalArgumentException if the account buyer is of type BusinessAccount and course has category which is not among the permitted for this account
     * @throws InsufficientBalanceException if the account does not have enough funds in its balance.
     * @throws CourseAlreadyPurchasedException if the course is already purchased for this account.
     * @throws MaxCourseCapacityReachedException if the account has reached the maximum allowed course capacity.
     */
    void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException;

    /**
     * Completes the given resources that belong to the given course provided that the course was previously purchased by this account.
     *
     * @param resourcesToComplete the resources which will be completed.
     * @param course the course in which the resources will be completed.
     * @throws IllegalArgumentException if course or resourcesToComplete are null.
     * @throws CourseNotPurchasedException if course is not currently purchased for this account.
     * @throws ResourceNotFoundException if a certain resource could not be found in the course.
     */
    void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException;

    /**
     * Completes the whole course.
     *
     * @param course the course which will be completed.
     * @param grade the grade with which the course will be completed.
     * @throws IllegalArgumentException if grade is not in range [2.00, 6.00] or course is null.
     * @throws CourseNotPurchasedException if course is not currently purchased for this account.
     * @throws CourseNotCompletedException if there is a resource in the course which is not completed.
     */
    void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException;

    /**
     * Gets the course with the least completion percentage. 
     * Returns null if the account does not have any purchased courses.
     */
    Course getLeastCompletedCourse();
}
