package bg.sofia.uni.fmi.mjt.spotify.database;

import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.song.Playlist;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class PlaylistsDatabaseTest {
    static List<Playlist> expectedPlaylists;

    StringReader stringReader;

    @BeforeAll
    static void setUpTestCase() {
        expectedPlaylists = List.of(
            new Playlist("playlist1", "author1", List.of("song1")),
            new Playlist("playlist2", "author2", List.of("song1", "song2")),
            new Playlist("playlist3", "author3", List.of("song1", "song2", "song3"))
        );
    }

    PlaylistsDatabase playlistsDatabase;

    @BeforeEach
    void setUp() throws IOException, UnsupportedAudioFileException {
        stringReader = new StringReader(
            "playlist1;author1;song1" + System.lineSeparator() +
                "playlist2;author2;song1,song2" + System.lineSeparator() +
                "playlist3;author3;song1,song2,song3" + System.lineSeparator()
        );

        playlistsDatabase = new PlaylistsDatabase();

        playlistsDatabase.load(stringReader);
    }

    @Test
    void testGetPlaylistByNameWithNoPresentName() {
        Playlist result = playlistsDatabase.getPlaylistByName("playlist0");

        assertNull(result, "getSongByTitle(...) should return null when playlist with such name is not present.");
    }

    @Test
    void testGetPlaylistByNameWithPresentName() {
        Playlist result = playlistsDatabase.getPlaylistByName("playlist1");

        Playlist expectedResult = new Playlist("playlist1", "author1", List.of("song1"));

        assertEquals(expectedResult, result,
            "getPlaylistByName(...) should return correct playlist when playlist with suck name is present.");
    }

    @Test
    void testCreatePlaylistWhenSuchExists() {
        assertThrows(SuchPlaylistAlreadyExistsException.class,
            () -> playlistsDatabase.createPlaylist("playlist1", "test1@test.test"),
            "createPlaylist(...) should throw SuchPlaylistAlreadyExistsException when called playlist name that already exists.");
    }

    @Test
    void testCreatePlaylistWhenSuchDoesNotExist() {
        assertDoesNotThrow(() -> playlistsDatabase.createPlaylist("playlist0", "test0@test.test"),
            "createPlaylist(...) should not throw when called with valid playlist name.");

        assertThrows(SuchPlaylistAlreadyExistsException.class,
            () -> playlistsDatabase.createPlaylist("playlist0", "test0@test.test"),
            "createPlaylist(...) should throw SuchPlaylistAlreadyExistsException when called playlist name that already exists.");
    }

}