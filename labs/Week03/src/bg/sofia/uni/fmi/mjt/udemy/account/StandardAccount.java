package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;

import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public class StandardAccount extends AccountBase {

    public StandardAccount(String username, double balance) {
        super(username, balance);
        accountType = AccountType.STANDARD;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        super.buyCourse(course);

        courses[coursesCount++] = course;
        course.purchase();

        balance -= course.getPrice();
    }
}
