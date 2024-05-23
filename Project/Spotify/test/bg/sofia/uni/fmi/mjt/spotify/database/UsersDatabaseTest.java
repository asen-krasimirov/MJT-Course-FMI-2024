package bg.sofia.uni.fmi.mjt.spotify.database;

import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.user.User;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class UsersDatabaseTest {
    static List<User> expectedUsers;

    StringReader stringReader;

    UsersDatabase usersDatabase;

    @BeforeAll
    static void setUpTestCase() {
        expectedUsers = List.of(
            new User("test1@test.test", "hashedpass1"),
            new User("test2@test.test", "hashedpass2"),
            new User("test3@test.test", "hashedpass3")
        );
    }

    @BeforeEach
    void setUp() throws IOException {
        stringReader = new StringReader(
            "test1@test.test;hashedpass1" + System.lineSeparator() +
                "test2@test.test;hashedpass2" + System.lineSeparator() +
                "test3@test.test;hashedpass3" + System.lineSeparator()
        );

        usersDatabase = new UsersDatabase();

        usersDatabase.load(stringReader);
    }

    @Test
    void testGetUserByEmailWithNoPresentEmail() {
        User result = usersDatabase.getUserByEmail("test0@test.test");

        assertNull(result, "getUserByEmail(...) should return null when user with such email is not present.");
    }

    @Test
    void testGetUserByEmailWithPresentEmail() {
        User result = usersDatabase.getUserByEmail("test1@test.test");

        assertEquals(expectedUsers.get(0), result,
            "getUserByEmail(...) should return correct user with such email when present.");
    }

    @Test
    void testAddUserWithExistingUser() {
        User newUser = new User("test1@test.test", "hashedpassword1");

        assertThrows(UserAlreadyRegisteredException.class, () -> usersDatabase.addUser(newUser),
            "addUser(...) should throw UserAlreadyRegisteredException when called with existing user.");
    }

    @Test
    void testAddUserWithNewUser() {
        User newUser = new User("test0@test.test", "hashedpassword0");

        assertDoesNotThrow(() -> usersDatabase.addUser(newUser),
            "addUser(...) should not throw when called with correct user.");
    }
}
