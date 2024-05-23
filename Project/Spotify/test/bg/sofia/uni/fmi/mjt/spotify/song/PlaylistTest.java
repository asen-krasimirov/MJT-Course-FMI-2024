package bg.sofia.uni.fmi.mjt.spotify.song;

import bg.sofia.uni.fmi.mjt.spotify.exception.database.IncorrectDataFormatException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PlaylistTest {
    @Test
    void testOfFactoryMethodWithIncorrectFormat() {
        String testLine1 = "playlist1,author1,song1";
        String testLine2 = "playlist1";

        assertThrows(IncorrectDataFormatException.class, () -> Playlist.of(testLine1),
            "of(...) should throw IncorrectDataFormatException when data is in incorrect format.");

        assertThrows(IncorrectDataFormatException.class, () -> Playlist.of(testLine2),
            "of(...) should throw IncorrectDataFormatException when data is in incorrect format.");
    }

    @Test
    void testOfFactoryMethodWithCorrectFormat() {
        String testLine = "playlist1;author1;song1";

        Playlist result = Playlist.of(testLine);

        Playlist expectedPlaylist = new Playlist("playlist1", "author1", List.of("song1"));

        assertEquals(expectedPlaylist.name(), result.name(),
            "of(...) should parse the correct user when called with valid data.");

        assertEquals(expectedPlaylist.author(), result.author(),
            "of(...) should parse the correct user when called with valid data.");

        assertEquals(expectedPlaylist.songTitles(), result.songTitles(),
            "of(...) should parse the correct user when called with valid data.");
    }

    @Test
    void testAddSong() {
        Playlist playlist = new Playlist("playlist1", "author1", new ArrayList<>(List.of("song1")));

        playlist.addSong("song2");

        assertEquals(List.of("song1", "song2"), playlist.songTitles(), "addSong(...) should add song to songTitles.");
    }

}
