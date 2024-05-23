package bg.sofia.uni.fmi.mjt.spotify.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class EmailValidatorTest {
    @Test
    void testIsEmailValidWithValidEmail() {
        String email = "test@test.test";

        assertTrue(EmailValidator.isValidEmail(email),
            "isValidEmail(...) should return true when called with valid email");
    }

    @Test
    void testIsEmailValidWithInvalidEmail() {
        String email1 = "invalid.email";
        String email2 = "invalid@.com";
        String email3 = "invalid@com";
        String email4 = "invalid@com";
        String email5 = "invalid@@test.com";
        String email6 = "@test.com";
        String email7 = "invalid@";
        String email8 = "invalid@test..test";

        assertFalse(EmailValidator.isValidEmail(email1),
            "isValidEmail(...) should return false when called with invalid email");

        assertFalse(EmailValidator.isValidEmail(email2),
            "isValidEmail(...) should return false when called with invalid email");

        assertFalse(EmailValidator.isValidEmail(email3),
            "isValidEmail(...) should return false when called with invalid email");

        assertFalse(EmailValidator.isValidEmail(email4),
            "isValidEmail(...) should return false when called with invalid email");

        assertFalse(EmailValidator.isValidEmail(email5),

            "isValidEmail(...) should return false when called with invalid email");
        assertFalse(EmailValidator.isValidEmail(email6),
            "isValidEmail(...) should return false when called with invalid email");

        assertFalse(EmailValidator.isValidEmail(email7),
            "isValidEmail(...) should return false when called with invalid email");

        assertFalse(EmailValidator.isValidEmail(email8),
            "isValidEmail(...) should return false when called with invalid email");
    }
}
