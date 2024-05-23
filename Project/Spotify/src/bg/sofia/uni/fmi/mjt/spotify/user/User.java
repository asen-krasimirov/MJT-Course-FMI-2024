package bg.sofia.uni.fmi.mjt.spotify.user;

import bg.sofia.uni.fmi.mjt.spotify.exception.database.IncorrectDataFormatException;
import static bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex.INDEX_ONE;
import static bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex.INDEX_ZERO;

import java.util.Objects;

public record User(String email, String password) {
    private static final int PARSE_ARGUMENTS_COUNT = 2;

    public static User of(String line) {
        String[] tokens = line.split("\\Q;\\E");

        if (tokens.length != PARSE_ARGUMENTS_COUNT) {
            throw new IncorrectDataFormatException(
                "Data of user not stored in correct format: <email>;<password> but is " + line);
        }

        String email = tokens[INDEX_ZERO.getIndex()];
        String password = tokens[INDEX_ONE.getIndex()];

        return new User(email, password);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof User user)) {
            return false;
        }

        return this.email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email);
    }
}
