package bg.sofia.uni.fmi.mjt.spotify.database;

import static bg.sofia.uni.fmi.mjt.spotify.config.SpotifyConfigTest.SONGS_DATA_DATABASE_TEST_SOURCE;
import bg.sofia.uni.fmi.mjt.spotify.exception.song.NoSongCurrentlyStreamedException;
import bg.sofia.uni.fmi.mjt.spotify.exception.song.NoSuchSongExistsException;
import bg.sofia.uni.fmi.mjt.spotify.song.Song;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.channels.SelectionKey;
import java.nio.file.Path;
import java.util.List;

public class SongsDatabaseTest {
    StringReader stringReader;

    SongsDatabase songsDatabase;

    @BeforeEach
    void setUp() throws IOException, UnsupportedAudioFileException {
        stringReader = new StringReader(
            "song1;author1;1" + System.lineSeparator() +
                "song2;author2;2" + System.lineSeparator() +
                "song3;author3;3" + System.lineSeparator()
        );

        songsDatabase = new SongsDatabase();

        songsDatabase.load(stringReader, Path.of(SONGS_DATA_DATABASE_TEST_SOURCE));
    }

    @Test
    void testGetSongByTitleWithNoPresentTitle() {
        Song result = songsDatabase.getSongByTitle("song0");

        assertNull(result, "getSongByTitle(...) should return null when song with such title is not present.");
    }

    @Test
    void testGetSongByTitleWithPresentTitle() {
        Song result = songsDatabase.getSongByTitle("song1");

        Song expectedResult = new Song("song1", "author1", 1);

        assertEquals(expectedResult, result,
            "getSongByTitle(...) should return correct song when song with such title is present.");
    }

    @Test
    void testPlaySongThatIsNotPresent() {
        SelectionKey mockClientKey = mock();

        assertThrows(NoSuchSongExistsException.class, () -> songsDatabase.playSong(mockClientKey, "song0"),
            "playSong(...) should throw NoSuchSongExistsException when called with song title that is not present.");
    }

    @Test
    void testPlaySongThatIsPresent() {
        SelectionKey mockClientKey = mock();

        assertDoesNotThrow(() -> songsDatabase.playSong(mockClientKey, "song1"),
            "playSong(...) should not throw when called with song title that is present.");

        assertEquals(2, songsDatabase.getSongByTitle("song1").getPlays());
    }

    @Test
    void testStopSongWhenNoSongIsBeingPlayed() {
        SelectionKey mockClientKey = mock();

        assertThrows(NoSongCurrentlyStreamedException.class, () -> songsDatabase.stopSong(mockClientKey),
            "stopSong(...) should throw NoSongCurrentlyStreamedException when called when no song is being played.");
    }

    @Test
    void testStopSongWhenSongIsBeingPlayed() {
        SelectionKey mockClientKey = mock();

        assertDoesNotThrow(() -> songsDatabase.playSong(mockClientKey, "song1"),
            "playSong(...) should not throw when called with song title that is present.");

        assertDoesNotThrow(() -> songsDatabase.stopSong(mockClientKey),
            "stopSong(...) should not throw when called when song is being played.");
    }

    @Test
    void testSearchSongsWithNoMatches() {
        List<String> result = songsDatabase.searchSongs(new String[] {"test"});

        assertEquals(List.of(), result,
            "searchSongs(...) should return empty List<String> when there is no match for any song.");
    }

    @Test
    void testSearchSongsWithMatches() {
        List<String> result = songsDatabase.searchSongs(new String[] {"1", "2"});

        List<String> expectedResult = List.of("song1", "song2");

        assertEquals(expectedResult, result,
            "searchSongs(...) should return List<String> with all matches.");
    }

    @Test
    void testSearchSongsWithAllMatched() {
        List<String> result = songsDatabase.searchSongs(new String[] {"song"});

        List<String> expectedResult = List.of("song1", "song2", "song3");

        assertEquals(expectedResult, result,
            "searchSongs(...) should return List<String> with all songs when all are matched.");
    }

    @Test
    void testSearchMostListenedSongsWithNegativeN() {
        assertThrows(IllegalArgumentException.class, () -> songsDatabase.searchMostListenedSongs(-1),
            "searchMostListenedSongs(...) should throw IllegalArgumentException when called with negative value of n.");
    }

    @Test
    void testSearchMostListenedSongsWithNLessThenTotal() {
        List<String> result = songsDatabase.searchMostListenedSongs(2);

        List<String> expectedResult = List.of("song3", "song2");

        assertEquals(expectedResult, result,
            "searchMostListenedSongs(...) should return correct song titles when called with n less then total.");
    }

    @Test
    void testSearchMostListenedSongsWithNEqualsTotal() {
        List<String> result = songsDatabase.searchMostListenedSongs(3);

        List<String> expectedResult = List.of("song3", "song2", "song1");

        assertEquals(expectedResult, result,
            "searchMostListenedSongs(...) should return all song titles when called with n equaling total.");
    }

    @Test
    void testSearchMostListenedSongsWithNMoreThenTotal() {
        List<String> result = songsDatabase.searchMostListenedSongs(4);

        List<String> expectedResult = List.of("song3", "song2", "song1");

        assertEquals(expectedResult, result,
            "searchMostListenedSongs(...) should return all song titles when called with n more then total.");
    }

}
