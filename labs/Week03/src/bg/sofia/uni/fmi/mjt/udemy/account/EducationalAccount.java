package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;

import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public class EducationalAccount extends AccountBase {
    private boolean hasDiscount = false;

    public EducationalAccount(String username, double balance) {
        super(username, balance);
        accountType = AccountType.EDUCATION;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        super.buyCourse(course);

        courses[coursesCount++] = course;
        course.purchase();

        if (hasDiscount) {
            double lastFiveGradesSum = 0;
            for (int i = gradesCount - 1; i >= 0; --i) {
                lastFiveGradesSum += grades[i];
            }

            if ((lastFiveGradesSum / 5.0) >= 4.5) {
                balance -= course.getPrice() - (course.getPrice() * accountType.getDiscount());
                hasDiscount = false;
                return;
            }
        }

        balance -= course.getPrice();
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        super.completeCourse(course, grade);

        if (gradesCount % 5 == 0) {
            hasDiscount = true;
        }
    }
}
