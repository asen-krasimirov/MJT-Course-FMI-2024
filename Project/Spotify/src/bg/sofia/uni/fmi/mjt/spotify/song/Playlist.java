package bg.sofia.uni.fmi.mjt.spotify.song;

import bg.sofia.uni.fmi.mjt.spotify.exception.database.IncorrectDataFormatException;
import static bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex.INDEX_ONE;
import static bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex.INDEX_TWO;
import static bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex.INDEX_ZERO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Playlist(String name, String author, List<String> songTitles) {
    private static final int PARSE_ARGUMENTS_UPPER_COUNT = 3;

    private static final int PARSE_ARGUMENTS_LOWER_COUNT = 2;

    public static Playlist of(String line) {
        String[] tokens = line.split("\\Q;\\E");

        if (tokens.length > PARSE_ARGUMENTS_UPPER_COUNT || tokens.length < PARSE_ARGUMENTS_LOWER_COUNT) {
            throw new IncorrectDataFormatException(
                "Data of user not stored in correct format: <name>;<author>;<songs> but is " + line);
        }

        String name = tokens[INDEX_ZERO.getIndex()];
        String author = tokens[INDEX_ONE.getIndex()];

        List<String> songs;
        if (tokens.length > 2) {
            songs = new ArrayList<>(List.of(tokens[INDEX_TWO.getIndex()].split("\\Q,\\E")));
        } else {
            songs = new ArrayList<>();
        }

        return new Playlist(name, author, songs);
    }

    public void addSong(String songTitle) {
        songTitles.add(songTitle);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Playlist playlist)) {
            return false;
        }

        return name().equals(playlist.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name());
    }
}
