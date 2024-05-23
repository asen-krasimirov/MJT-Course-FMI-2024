package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.database.PlaylistsDatabase;
import bg.sofia.uni.fmi.mjt.spotify.database.SongsDatabase;
import bg.sofia.uni.fmi.mjt.spotify.database.UsersDatabase;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.UserNotLoggedException;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.NoAccessToPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.NoSuchPlaylistExistsException;
import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.exception.song.NoSongCurrentlyStreamedException;
import bg.sofia.uni.fmi.mjt.spotify.exception.song.NoSuchSongExistsException;
import bg.sofia.uni.fmi.mjt.spotify.song.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.song.Song;
import bg.sofia.uni.fmi.mjt.spotify.user.User;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpotifyTest {
    SpotifyAPI spotify;

    @Mock
    UsersDatabase usersDatabase;

    @Mock
    SongsDatabase songsDatabase;

    @Mock
    PlaylistsDatabase playlistsDatabase;

    @Mock
    Map<SelectionKey, User> users;

    @Mock
    SelectionKey clientKey;

    @BeforeEach
    void setUp() {
        usersDatabase = mock();
        songsDatabase = mock();
        playlistsDatabase = mock();
        users = mock();

        spotify = new Spotify(usersDatabase, songsDatabase, playlistsDatabase, users);
    }

    @Test
    void testRegisterUserExistingUser() throws UserAlreadyRegisteredException {
        String email = "test1@test.test";
        String password = "hashedpassword1";
        User user = new User(email, password);

        doThrow(UserAlreadyRegisteredException.class).when(usersDatabase).addUser(user);

        assertThrows(UserAlreadyRegisteredException.class, () -> spotify.registerUser(clientKey, email, password),
            "registerUser(...) should throw UserAlreadyRegisteredException when trying to add user that already exists.");
    }

    @Test
    void testRegisterUserNewUser() throws UserAlreadyRegisteredException {
        String email = "test0@test.test";
        String password = "hashedpassword0";
        User user = new User(email, password);

        assertDoesNotThrow(() -> spotify.registerUser(clientKey, email, password),
            "registerUser(...) should not throw when trying to add user that does not exist.");

        verify(usersDatabase, times(1)).addUser(user);

        verify(users, times(1)).put(clientKey, user);
    }

    @Test
    void testLoginUserNewUser() {
        String email = "test0@test.test";
        String password = "hashedpassword0";

        when(usersDatabase.getUserByEmail(email)).thenReturn(null);

        assertThrows(UserNotRegisteredException.class, () -> spotify.loginUser(clientKey, email, password),
            "loginUser(...) should throw UserNotRegisteredException when trying to login into user that does not exist.");
    }

    @Test
    void testLoginUserExistingUserWithWrongPassword() {
        String email = "test0@test.test";
        String password = "hashedpassword1";
        User user = new User(email, "hashedpassword0");

        when(usersDatabase.getUserByEmail(email)).thenReturn(user);

        assertThrows(WrongPasswordException.class, () -> spotify.loginUser(clientKey, email, password),
            "loginUser(...) should throw WrongPasswordException when trying to login into user with wrong password.");
    }

    @Test
    void testLoginUserExistingUserWithCorrectPassword() {
        String email = "test0@test.test";
        String password = "hashedpassword0";
        User user = new User(email, password);

        when(usersDatabase.getUserByEmail(email)).thenReturn(user);

        assertDoesNotThrow(() -> spotify.loginUser(clientKey, email, password),
            "loginUser(...) should not throw when trying to login into user with correct password.");

        verify(users, times(1)).put(clientKey, user);
    }

    @Test
    void testDisconnectUserWhenUserNotLogged() {
        when(users.containsKey(clientKey)).thenReturn(false);

        assertThrows(UserNotLoggedException.class, () -> spotify.disconnectUser(clientKey),
            "disconnectUser(...) should throw UserNotLoggedException when called with clientKey that is not logged.");
    }

    @Test
    void testDisconnectUserWhenUserIsLogged() {
        when(users.containsKey(clientKey)).thenReturn(true);

        assertDoesNotThrow(() -> spotify.disconnectUser(clientKey),
            "disconnectUser(...) should not throw when called with clientKey that is logged.");

        verify(users, times(1)).remove(clientKey);
    }

    @Test
    void testPlaySongWhenUserNotLogged() {
        String title = "song1";

        when(users.containsKey(clientKey)).thenReturn(false);

        assertThrows(UserNotLoggedException.class, () -> spotify.playSong(clientKey, title),
            "playSong(...) should throw UserNotLoggedException when called with clientKey that is not logged.");
    }

    @Test
    void testPlaySongWhenUserIsLoggedAndNoSuchSongExists() throws NoSuchSongExistsException {
        String title = "song1";

        when(users.containsKey(clientKey)).thenReturn(true);

        doThrow(NoSuchSongExistsException.class).when(songsDatabase).playSong(clientKey, title);

        assertThrows(NoSuchSongExistsException.class, () -> spotify.playSong(clientKey, title),
            "playSong(...) should throw NoSuchSongExistsException when called with title witch there is not song with it.");
    }

    @Test
    void testPlaySongWhenUserIsLoggedAndSuchSongExists() throws NoSuchSongExistsException {
        String title = "song1";

        when(users.containsKey(clientKey)).thenReturn(true);

        assertDoesNotThrow(() -> spotify.playSong(clientKey, title),
            "playSong(...) should not throw when called with title witch there is song with it.");

        verify(songsDatabase, times(1)).playSong(clientKey, title);
    }

    @Test
    void testStopSongWhenUserNotLogged() {
        when(users.containsKey(clientKey)).thenReturn(false);

        assertThrows(UserNotLoggedException.class, () -> spotify.stopSong(clientKey),
            "stopSong(...) should throw UserNotLoggedException when called with clientKey that is not logged.");
    }

    @Test
    void testStopSongWhenUserIsLoggedAndNoSongIsBeingPlayed() throws NoSongCurrentlyStreamedException {
        when(users.containsKey(clientKey)).thenReturn(true);

        doThrow(NoSongCurrentlyStreamedException.class).when(songsDatabase).stopSong(clientKey);

        assertThrows(NoSongCurrentlyStreamedException.class, () -> spotify.stopSong(clientKey),
            "stopSong(...) should throw NoSongCurrentlyStreamedException when called when no song is currently being played.");
    }

    @Test
    void testStopSongWhenUserIsLoggedAndSongIsBeingPlayed() throws NoSongCurrentlyStreamedException {
        when(users.containsKey(clientKey)).thenReturn(true);

        assertDoesNotThrow(() -> spotify.stopSong(clientKey),
            "stopSong(...) should not throw when called when song is currently being played.");

        verify(songsDatabase, times(1)).stopSong(clientKey);
    }

    @Test
    void testSearchSongsWhenUserNotLogged() {
        when(users.containsKey(clientKey)).thenReturn(false);

        assertThrows(UserNotLoggedException.class, () -> spotify.searchSongs(clientKey, new String[] {"test"}),
            "searchSongs(...) should throw UserNotLoggedException when called with clientKey that is not logged.");
    }

    @Test
    void testSearchSongsWhenUserIsLoggedAndThereAreNoMatches() {
        when(users.containsKey(clientKey)).thenReturn(true);

        String[] words = {"test"};

        when(songsDatabase.searchSongs(words)).thenReturn(List.of());

        assertThrows(NoSuchSongExistsException.class, () -> spotify.searchSongs(clientKey, words),
            "searchSongs(...) should throw NoSuchSongExistsException when called with words that are not in any song.");
    }

    @Test
    void testSearchSongsWhenUserIsLoggedAndThereAreMatches() throws UserNotLoggedException, NoSuchSongExistsException {
        when(users.containsKey(clientKey)).thenReturn(true);

        String[] words = {"song1", "2"};

        when(songsDatabase.searchSongs(words)).thenReturn(List.of("song1", "song2"));

        List<String> expectedResult = List.of("song1", "song2");

        assertEquals(expectedResult, spotify.searchSongs(clientKey, words),
            "searchSongs(...) should return correct song titles that are matched.");
    }

    @Test
    void testSearchMostListenedSongsWhenUserNotLogged() {
        when(users.containsKey(clientKey)).thenReturn(false);

        assertThrows(UserNotLoggedException.class, () -> spotify.searchMostListenedSongs(clientKey, 1),
            "searchMostListenedSongs(...) should throw UserNotLoggedException when called with clientKey that is not logged.");
    }

    @Test
    void testSearchMostListenedSongsWhenUserIsLogged() throws UserNotLoggedException {
        when(users.containsKey(clientKey)).thenReturn(true);

        when(songsDatabase.searchMostListenedSongs(2)).thenReturn(List.of("song1", "song2"));

        List<String> expectedResult = List.of("song1", "song2");

        assertEquals(expectedResult, spotify.searchMostListenedSongs(clientKey, 2),
            "searchMostListenedSongs(...) should return correct top N songs.");
    }

    @Test
    void testGetPlaylistByNameWhenUserNotLogged() {
        when(users.containsKey(clientKey)).thenReturn(false);

        assertThrows(UserNotLoggedException.class, () -> spotify.getPlaylistByName(clientKey, "test"),
            "getPlaylistByName(...) should throw UserNotLoggedException when called with clientKey that is not logged.");
    }

    @Test
    void testGetPlaylistByNameWhenUserIsLoggedAndPlaylistDoesNotExist() {
        when(users.containsKey(clientKey)).thenReturn(true);

        String name = "playlist1";

        when(playlistsDatabase.getPlaylistByName(name)).thenReturn(null);

        assertThrows(NoSuchPlaylistExistsException.class, () -> spotify.getPlaylistByName(clientKey, name),
            "getPlaylistByName(...) should throw NoSuchPlaylistExistsException when called with playlist name that does not exist.");
    }

    @Test
    void testGetPlaylistByNameWhenUserIsLoggedAndPlaylistDoesExist()
        throws UserNotLoggedException, NoSuchPlaylistExistsException {
        when(users.containsKey(clientKey)).thenReturn(true);

        String name = "playlist1";

        Playlist playlist = new Playlist(name, "author1", new ArrayList<>(List.of("song1")));

        when(playlistsDatabase.getPlaylistByName(name)).thenReturn(playlist);

        Playlist expectedResult = new Playlist(name, "author1", new ArrayList<>(List.of("song1")));

        assertEquals(expectedResult, spotify.getPlaylistByName(clientKey, name),
            "getPlaylistByName(...) should return correct playlist when called with playlist name that does exist.");
    }

    @Test
    void testCreatePlaylistWhenUserNotLogged() {
        when(users.containsKey(clientKey)).thenReturn(false);

        assertThrows(UserNotLoggedException.class, () -> spotify.createPlaylist(clientKey, "playlist1"),
            "createPlaylist(...) should throw UserNotLoggedException when called with clientKey that is not logged.");
    }

    @Test
    void testCreatePlaylistWhenUserIsLoggedAndPlaylistAlreadyExists() throws SuchPlaylistAlreadyExistsException {
        when(users.containsKey(clientKey)).thenReturn(true);

        String name = "playlist1";

        User user = new User("test1@test.test", "hashedpassword1");

        when(users.get(clientKey)).thenReturn(user);

        doThrow(SuchPlaylistAlreadyExistsException.class).when(playlistsDatabase).createPlaylist(name, user.email());

        assertThrows(SuchPlaylistAlreadyExistsException.class, () -> spotify.createPlaylist(clientKey, name),
            "createPlaylist(...) should throw SuchPlaylistAlreadyExistsException when called with playlist name that already exists.");
    }

    @Test
    void testCreatePlaylistWhenUserIsLoggedAndPlaylistDoesNotExist() throws SuchPlaylistAlreadyExistsException {
        when(users.containsKey(clientKey)).thenReturn(true);

        String name = "playlist1";

        User user = new User("test1@test.test", "hashedpassword1");

        when(users.get(clientKey)).thenReturn(user);

        assertDoesNotThrow(() -> spotify.createPlaylist(clientKey, name),
            "createPlaylist(...) should not throw when called with playlist name that does not exist.");

        verify(playlistsDatabase, times(1)).createPlaylist(name, user.email());
    }

    @Test
    void testAddSongToWhenUserNotLogged() {
        when(users.containsKey(clientKey)).thenReturn(false);

        assertThrows(UserNotLoggedException.class, () -> spotify.addSongTo(clientKey, "playlist1", "song1"),
            "addSongTo(...) should throw UserNotLoggedException when called with clientKey that is not logged.");
    }

    @Test
    void testAddSongToWhenUserIsLoggedAndPlaylistDoesNotExist() {
        when(users.containsKey(clientKey)).thenReturn(true);

        String playlistName = "playlist1";
        String songTitle = "song1";

        when(playlistsDatabase.getPlaylistByName(playlistName)).thenReturn(null);

        assertThrows(NoSuchPlaylistExistsException.class, () -> spotify.addSongTo(clientKey, playlistName, songTitle),
            "addSongTo(...) should throw NoSuchPlaylistExistsException when called with playlist name that does not exist.");
    }

    @Test
    void testAddSongToWhenUserIsLoggedAndUserDoesNotOwnPlaylist() {
        when(users.containsKey(clientKey)).thenReturn(true);

        String playlistName = "playlist1";
        String songTitle = "song1";

        User user = new User("test0@test.test", "hashedpassword0");

        Playlist playlist = new Playlist(playlistName, "test1@test.test", new ArrayList<>());

        when(users.get(clientKey)).thenReturn(user);
        when(playlistsDatabase.getPlaylistByName(playlistName)).thenReturn(playlist);

        assertThrows(NoAccessToPlaylistException.class, () -> spotify.addSongTo(clientKey, playlistName, songTitle),
            "addSongTo(...) should throw NoAccessToPlaylistException when called with playlist that the current user does not own.");
    }

    @Test
    void testAddSongToWhenUserIsLoggedAndSnogDoesNotExist() {
        when(users.containsKey(clientKey)).thenReturn(true);

        String playlistName = "playlist1";
        String songTitle = "song1";

        User user = new User("test1@test.test", "hashedpassword1");

        Playlist playlist = new Playlist(playlistName, "test1@test.test", new ArrayList<>());

        when(users.get(clientKey)).thenReturn(user);
        when(playlistsDatabase.getPlaylistByName(playlistName)).thenReturn(playlist);
        when(songsDatabase.getSongByTitle(songTitle)).thenReturn(null);

        assertThrows(NoSuchSongExistsException.class, () -> spotify.addSongTo(clientKey, playlistName, songTitle),
            "addSongTo(...) should throw NoSuchSongExistsException when called with song title that does not exist.");
    }

    @Test
    void testAddSongToWhenUserIsLoggedAndAllDataIsValid() {
        when(users.containsKey(clientKey)).thenReturn(true);

        String playlistName = "playlist1";
        String songTitle = "song1";

        User user = new User("test1@test.test", "hashedpassword1");
        Playlist playlist = new Playlist(playlistName, "test1@test.test", new ArrayList<>());
        Song song = new Song(songTitle, "author1", 1);

        when(users.get(clientKey)).thenReturn(user);
        when(playlistsDatabase.getPlaylistByName(playlistName)).thenReturn(playlist);
        when(songsDatabase.getSongByTitle(songTitle)).thenReturn(song);

        assertDoesNotThrow(() -> spotify.addSongTo(clientKey, playlistName, songTitle),
            "addSongTo(...) should not throw when called with all valid data.");

        List<String> expectedSongTitlesInPlaylist = List.of("song1");

        assertEquals(expectedSongTitlesInPlaylist, playlist.songTitles(),
            "addSongTo(...) should add song to playlist's songTitles when called with valid data.");
    }
}
