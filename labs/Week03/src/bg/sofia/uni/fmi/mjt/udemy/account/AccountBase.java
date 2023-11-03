package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;

public abstract class AccountBase implements Account {
    static int MaxCoursesCapacity = 100;
    protected AccountType accountType;
    private final String username;
    protected double balance;
    protected Course[] courses;
    protected int coursesCount;
    protected double[] grades;
    protected int gradesCount;

    public AccountBase(String username, double balance) {
        this.username = username;
        this.balance = balance;
        courses = new Course[MaxCoursesCapacity];
        coursesCount = 0;
        grades = new double[MaxCoursesCapacity];
        gradesCount = 0;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void addToBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Value of amount shouldn't be negative!");
        }

        balance += amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (course == null) {
            throw new IllegalArgumentException("Value of course shouldn't be null!");
        }

        if (balance < course.getPrice()) {
            throw new InsufficientBalanceException();
        }

        // could be course.isPurchased();
        for (int i = 0 ; i < coursesCount; ++i) {
            if (courses[i].equals(course)) {
                throw new CourseAlreadyPurchasedException();
            }
        }

        if (coursesCount >= MaxCoursesCapacity) {
            throw new MaxCourseCapacityReachedException();
        }
    }

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException {
        if (course == null || resourcesToComplete == null) {
            throw new IllegalArgumentException("Value of course and value of resourcesToComplete shouldn't be null!");
        }

        for (int i = 0 ; i < coursesCount; ++i) {
            if (courses[i].equals(course)) {
                for (Resource resource: resourcesToComplete) {
                    courses[i].completeResource(resource);
                }
                return;
            }
        }

        throw new CourseNotPurchasedException();
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if (course == null || grade < 2.00 || grade > 6.00) {
            throw new IllegalArgumentException("Value of course shouldn't be null and grade should be from 2.00 to 6.00");
        }

        for (int i = 0 ; i < coursesCount; ++i) {
            if (courses[i].equals(course)) {
                if (!courses[i].isCompleted()) {
                    throw new CourseNotCompletedException();
                }

                courses[i] = courses[coursesCount--];
                grades[gradesCount++] = grade;

                return;
            }
        }

        throw new CourseNotPurchasedException();
    }

    @Override
    public Course getLeastCompletedCourse() {
        Course curLeast = null;
        double curLeastPercentage = 100.0;

        for (int i = 0; i < coursesCount; ++i) {
            double curPercentage = courses[i].getCompletionPercentage();

            if (curPercentage < curLeastPercentage) {
                curLeast = courses[i];
                curLeastPercentage = curPercentage;
            }
        }

        return curLeast;
    }
}
