package bg.sofia.uni.fmi.mjt.spotify.database;

import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

public class UsersDatabase {
    private final Set<User> users = new HashSet<>();

    public void load(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            users.add(User.of(line));
        }
    }

    public void save(Writer writer) throws IOException {
        for (User user : users) {
            writer.write(user.email() + ";" + user.password() + System.lineSeparator());
        }
    }

    public User getUserByEmail(String email) {
        return users
            .stream()
            .filter(user -> user.email().equals(email))
            .findFirst()
            .orElse(null);
    }

    public void addUser(User user) throws UserAlreadyRegisteredException {
        if (users.contains(user)) {
            throw new UserAlreadyRegisteredException("A user with that email is already registered!");
        }

        users.add(user);
    }
}
