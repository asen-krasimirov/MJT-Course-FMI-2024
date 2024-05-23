package bg.sofia.uni.fmi.mjt.spotify.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PasswordHashTest {
    @Test
    void testHashPasswordWithValidPassword() {
        String password = "passwordToHash";

        String expectedResult = "FVFmN3+qUZMsMmdgjkyVzR5wdo5phV+aU2PoMSejLZI=";

        assertEquals(expectedResult, PasswordHash.hashPassword(password),
            "hashPassword(...) should return the password in hashed format.");

        assertEquals(expectedResult, PasswordHash.hashPassword(password),
            "hashPassword(...) should return consistently the same hash.");
    }
}
