package bg.sofia.uni.fmi.mjt.spotify.user;

import bg.sofia.uni.fmi.mjt.spotify.exception.database.IncorrectDataFormatException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void testOfFactoryMethodWithIncorrectFormat() {
        String testLine1 = "test@test.test,hashedpassword";
        String testLine2 = "test@test.test";

        assertThrows(IncorrectDataFormatException.class, () -> User.of(testLine1),
            "of(...) should throw IncorrectDataFormatException when data is in incorrect format.");

        assertThrows(IncorrectDataFormatException.class, () -> User.of(testLine2),
            "of(...) should throw IncorrectDataFormatException when data is in incorrect format.");
    }

    @Test
    void testOfFactoryMethodWithCorrectFormat() {
        String testLine = "test@test.test;hashedpassword";

        User result = User.of(testLine);

        User expectedUser = new User("test@test.test", "hashedpassword");

        assertEquals(expectedUser.email(), result.email(),
            "of(...) should parse the correct user when called with valid data.");

        assertEquals(expectedUser.password(), result.password(),
            "of(...) should parse the correct user when called with valid data.");
    }
}
