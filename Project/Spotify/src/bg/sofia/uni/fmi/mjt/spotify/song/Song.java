package bg.sofia.uni.fmi.mjt.spotify.song;

import bg.sofia.uni.fmi.mjt.spotify.exception.database.IncorrectDataFormatException;
import static bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex.INDEX_ONE;
import static bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex.INDEX_TWO;
import static bg.sofia.uni.fmi.mjt.spotify.util.TokenIndex.INDEX_ZERO;

import java.util.Objects;

public class Song {
    private static final int PARSE_ARGUMENTS_COUNT = 3;

    private final String title;

    private final String author;

    private int plays;

    private String songPath;

    public Song(String title, String author, int plays) {
        this.title = title;
        this.author = author;
        this.plays = plays;
    }

    public static Song of(String line) {
        String[] tokens = line.split("\\Q;\\E");

        if (tokens.length != PARSE_ARGUMENTS_COUNT) {
            throw new IncorrectDataFormatException(
                "Data of user not stored in correct format: <title>;<author>;<plays> but is " + line);
        }

        String title = tokens[INDEX_ZERO.getIndex()];
        String author = tokens[INDEX_ONE.getIndex()];
        int plays = Integer.parseInt(tokens[INDEX_TWO.getIndex()]);

        return new Song(title, author, plays);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPlays() {
        return plays;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public void play() {
        plays++;
    }

    public boolean isAMatch(String[] words) {
        for (String word : words) {
            if (title.contains(word) || author.contains(word)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Song song)) {
            return false;
        }

        return title.equals(song.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
