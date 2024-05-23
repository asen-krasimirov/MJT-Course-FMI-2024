package bg.sofia.uni.fmi.mjt.spotify.song;

import bg.sofia.uni.fmi.mjt.spotify.exception.database.IncorrectDataFormatException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class SongTest {
    @Test
    void testOfFactoryMethodWithIncorrectFormat() {
        String testLine1 = "song1,author1,1";
        String testLine2 = "song1;author1";

        assertThrows(IncorrectDataFormatException.class, () -> Song.of(testLine1),
            "of(...) should throw IncorrectDataFormatException when data is in incorrect format.");

        assertThrows(IncorrectDataFormatException.class, () -> Song.of(testLine2),
            "of(...) should throw IncorrectDataFormatException when data is in incorrect format.");
    }

    @Test
    void testOfFactoryMethodWithCorrectFormat() {
        String testLine = "song1;author1;1";

        Song result = Song.of(testLine);

        Song expectedSong = new Song("song1", "author1", 1);

        assertEquals(expectedSong.getTitle(), result.getTitle(),
            "of(...) should parse the correct song when called with valid data.");

        assertEquals(expectedSong.getAuthor(), result.getAuthor(),
            "of(...) should parse the correct song when called with valid data.");

        assertEquals(expectedSong.getPlays(), result.getPlays(),
            "of(...) should parse the correct song when called with valid data.");
    }

    @Test
    void testPlay() {
        Song song = new Song("song1", "author1", 1);

        song.play();

        assertEquals(2, song.getPlays(), "play() should increments plays by 1.");
    }

    @Test
    void testIsAMatchWithNoMatch() {
        Song song = new Song("song1", "author1", 1);

        assertFalse(song.isAMatch(new String[] {"test", "to", "2"}),
            "isAMatch() should return false when called with data where there is no match.");
    }

    @Test
    void testIsAMatchWithMatch() {
        Song song = new Song("song1", "author1", 1);

        assertTrue(song.isAMatch(new String[] {"so"}),
            "isAMatch() should return true when called with data where there is a match in title.");

        assertTrue(song.isAMatch(new String[] {"au"}),
            "isAMatch() should return true when called with data where there is a match in author.");

        assertTrue(song.isAMatch(new String[] {"1"}),
            "isAMatch() should return true when called with data where there is a match in song and author.");
    }

}
