package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;

import bg.sofia.uni.fmi.mjt.udemy.account.AccountBase;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public class Udemy implements LearningPlatform {
    private Account[] accounts;
    private Course[] courses;

    public Udemy(Account[] accounts, Course[] courses) {
        this.accounts = accounts;
        this.courses = courses;
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalAccessError("Value of name shouldn't be null or blank!");
        }

        for (Course course: courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }

        throw new CourseNotFoundException();
    }

    private boolean isKeyword(String keyword) {
        char[] chars = keyword.toCharArray();

        for (char aChar : chars) {
            if ('a' > aChar || aChar > 'z') {
                if ('A' > aChar || aChar > 'Z') {
                    return false;
                }
            }
        }

        return true;
    }
    @Override
    public Course[] findByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Value of keyword shouldn't be null or blank!");
        }
        if (!isKeyword(keyword)) {
            throw new IllegalArgumentException("Value of keyword should be a valid keyword!");
        }

        int[] indexesToAdd = new int[courses.length];
        int ctr = 0;

        for (int i = 0; i < courses.length; ++i) {
            Course course = courses[i];
            if (course.getName().contains(keyword) || course.getDescription().contains(keyword)) {
                indexesToAdd[ctr++] = i;
            }
        }

        Course[] coursesToReturn = new Course[ctr];
        for (int i = 0; i < ctr; ++i) {
            coursesToReturn[i] = courses[indexesToAdd[i]];
        }

        return coursesToReturn;
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Value of category shouldn't be null");
        }

        int[] indexesToAdd = new int[courses.length];
        int ctr = 0;

        for (int i = 0; i < courses.length; ++i) {
            if (courses[i].getCategory() == category) {
                indexesToAdd[ctr++] = i;
            }
        }

        Course[] coursesToReturn = new Course[ctr];
        for (int i = 0; i < ctr; ++i) {
            coursesToReturn[i] = courses[indexesToAdd[i]];
        }

        return coursesToReturn;
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Value of name shouldn't be null or blank!");
        }

        for (Account account: accounts) {
            if (account.getUsername().equals(name)) {
                return account;
            }
        }

        throw new AccountNotFoundException();
    }

    @Override
    public Course getLongestCourse() {
        if (courses.length == 0) {
            return null;
        }

        Course longestCourse = courses[0];
        for (int i = 1; i < courses.length; ++i) {
            if (CourseDuration.compare(longestCourse.getTotalTime(), courses[i].getTotalTime()) == -1) {
                longestCourse = courses[i];
            }
        }

        return longestCourse;
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        if (courses.length == 0) {
            return null;
        }
        if (category == null) {
            throw new IllegalArgumentException("Value of category shouldn't be null!");
        }

        Course cheapest = null;
        for (Course course: courses) {
            if (course.getCategory() == category) {
                if (cheapest == null) {
                    cheapest = course;
                }
                else if (cheapest.getPrice() > course.getPrice()) {
                    cheapest = course;
                }
            }
        }

        return cheapest;
    }
}
