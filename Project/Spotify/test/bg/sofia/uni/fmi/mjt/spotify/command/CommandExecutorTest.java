package bg.sofia.uni.fmi.mjt.spotify.command;

import bg.sofia.uni.fmi.mjt.spotify.SpotifyAPI;
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
import bg.sofia.uni.fmi.mjt.spotify.user.PasswordHash;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

public class CommandExecutorTest {
    CommandExecutor commandExecutor;

    @Mock
    SpotifyAPI spotify;

    @Mock
    SelectionKey clientKey;

    @BeforeEach
    void setUp() {
        spotify = mock();

        commandExecutor = new CommandExecutor(spotify);
    }

    @Test
    void testExecuteUnknownCommand() {
        Command command = CommandCreator.newCommand("unknown-command");

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Unknown command";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with unknown command.");
    }

    @Test
    void testExecuteRegisterUserWithNotCorrectArgumentsCount() {
        Command command1 = CommandCreator.newCommand("register test1@test.test");
        String result1 = commandExecutor.execute(clientKey, command1);
        String expectedResult1 =
            "Invalid count of arguments: \"register\" expects 2 arguments. Example: \"register <email> <password>\"";

        assertEquals(expectedResult1, result1,
            "execute(...) should return correct response when called with \"register\" and not correct argument count.");

        Command command2 = CommandCreator.newCommand("register test1@test.test hashedpassword1 test");
        String result2 = commandExecutor.execute(clientKey, command2);
        String expectedResult2 =
            "Invalid count of arguments: \"register\" expects 2 arguments. Example: \"register <email> <password>\"";

        assertEquals(expectedResult2, result2,
            "execute(...) should return correct response when called with \"register\" and not correct argument count.");
    }

    @Test
    void testExecuteRegisterUserWithEmailNotInValidFormat() {
        Command command = CommandCreator.newCommand("register test1@@..test hashedpassword1");
        String result = commandExecutor.execute(clientKey, command);
        String expectedResult = "Error: Email is not the correct format.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"register\" and not valid email format.");
    }

    @Test
    void testExecuteRegisterUserWhoIsAlreadyRegistered() throws UserAlreadyRegisteredException {
        String email = "test1@test.test";
        String password = "hashedpassword1";

        Command command = CommandCreator.newCommand(String.format("register %s %s", email, password));

        doThrow(new UserAlreadyRegisteredException("A user with that email is already registered!")).when(spotify)
            .registerUser(clientKey, email, PasswordHash.hashPassword(password));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: A user with that email is already registered!";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"register\" and email that is already registered.");
    }

    @Test
    void testExecuteRegisterUserWithValidData() throws UserAlreadyRegisteredException {
        String email = "test1@test.test";
        String password = "hashedpassword1";

        Command command = CommandCreator.newCommand(String.format("register %s %s", email, password));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "You successfully registered.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"register\" with valid data.");

        verify(spotify, times(1)).registerUser(clientKey, email, PasswordHash.hashPassword(password));
    }


    @Test
    void testExecuteLoginUserWithNotCorrectArgumentsCount() {
        Command command1 = CommandCreator.newCommand("login test1@test.test");
        String result1 = commandExecutor.execute(clientKey, command1);
        String expectedResult1 =
            "Invalid count of arguments: \"login\" expects 2 arguments. Example: \"login <email> <password>\"";

        assertEquals(expectedResult1, result1,
            "execute(...) should return correct response when called with \"login\" and not correct argument count.");

        Command command2 = CommandCreator.newCommand("login test1@test.test hashedpassword1 test");
        String result2 = commandExecutor.execute(clientKey, command2);
        String expectedResult2 =
            "Invalid count of arguments: \"login\" expects 2 arguments. Example: \"login <email> <password>\"";

        assertEquals(expectedResult2, result2,
            "execute(...) should return correct response when called with \"login\" and not correct argument count.");
    }

    @Test
    void testExecuteLoginUserWithUserNotRegistered() throws UserNotRegisteredException, WrongPasswordException {
        String email = "test1@test.test";
        String password = "hashedpassword1";

        Command command = CommandCreator.newCommand(String.format("login %s %s", email, password));

        doThrow(new UserNotRegisteredException("No user with such email registered yet.")).when(spotify)
            .loginUser(clientKey, email, PasswordHash.hashPassword(password));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: No user with such email registered yet.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"login\" and user data that is not registered yet.");
    }

    @Test
    void testExecuteLoginUserWithWrongPassword() throws UserNotRegisteredException, WrongPasswordException {
        String email = "test1@test.test";
        String password = "hashedpassword0";

        Command command = CommandCreator.newCommand(String.format("login %s %s", email, password));

        doThrow(new WrongPasswordException("Invalid password was provided.")).when(spotify)
            .loginUser(clientKey, email, PasswordHash.hashPassword(password));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: Invalid password was provided.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"login\" and wrong password.");
    }

    @Test
    void testExecuteLoginUserWithValidData() throws UserNotRegisteredException, WrongPasswordException {
        String email = "test1@test.test";
        String password = "hashedpassword1";

        Command command = CommandCreator.newCommand(String.format("login %s %s", email, password));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "You successfully logged in.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"login\" and valid data.");

        verify(spotify, times(1)).loginUser(clientKey, email, PasswordHash.hashPassword(password));
    }

    @Test
    void testExecuteDisconnectUserWhoIsNotLogged() throws UserNotLoggedException {
        Command command = CommandCreator.newCommand("disconnect");

        doThrow(new UserNotLoggedException("User is not logged.")).when(spotify).disconnectUser(clientKey);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: User is not logged.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"disconnect\" and user is not logged.");
    }

    @Test
    void testExecuteDisconnectUserWhoIsLogged() throws UserNotLoggedException {
        Command command = CommandCreator.newCommand("disconnect");

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "You successfully disconnected.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"disconnect\" and user is logged.");

        verify(spotify, times(1)).disconnectUser(clientKey);
    }

    @Test
    void testExecutePlaySongWithNotCorrectArgumentsCount() {
        String title = "song1";

        Command command1 = CommandCreator.newCommand("play");
        String result1 = commandExecutor.execute(clientKey, command1);
        String expectedResult1 =
            "Invalid count of arguments: \"play\" expects 1 arguments. Example: \"play <song>\"";

        assertEquals(expectedResult1, result1,
            "execute(...) should return correct response when called with \"play\" and not correct argument count.");

        Command command2 = CommandCreator.newCommand(String.format("play %s test", title));
        String result2 = commandExecutor.execute(clientKey, command2);
        String expectedResult2 =
            "Invalid count of arguments: \"play\" expects 1 arguments. Example: \"play <song>\"";

        assertEquals(expectedResult2, result2,
            "execute(...) should return correct response when called with \"play\" and not correct argument count.");
    }

    @Test
    void testExecutePlaySongWithUserNotLogged() throws UserNotLoggedException, NoSuchSongExistsException {
        String title = "song1";

        Command command = CommandCreator.newCommand(String.format("play %s", title));

        doThrow(new UserNotLoggedException("User is not logged.")).when(spotify).playSong(clientKey, title);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: User is not logged.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"play\" and user is not logged in.");
    }

    @Test
    void testExecutePlaySongWithNoSuchSongExisting() throws UserNotLoggedException, NoSuchSongExistsException {
        String title = "song1";

        Command command = CommandCreator.newCommand(String.format("play %s", title));

        doThrow(new NoSuchSongExistsException("No song with such title exists.")).when(spotify)
            .playSong(clientKey, title);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: No song with such title exists.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"play\" and no such song exists.");
    }

    @Test
    void testExecutePlaySongWithValidData() throws UserNotLoggedException, NoSuchSongExistsException {
        String title = "song1";

        Command command = CommandCreator.newCommand(String.format("play %s", title));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Now playing: " + title;

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"play\" with valid data.");

        verify(spotify, times(1)).playSong(clientKey, title);
    }

    @Test
    void testExecuteStopSongWithUserNotLogged() throws UserNotLoggedException, NoSongCurrentlyStreamedException {
        Command command = CommandCreator.newCommand("stop");

        doThrow(new UserNotLoggedException("User is not logged.")).when(spotify).stopSong(clientKey);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: User is not logged.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"stop\" and user is not logged in.");
    }

    @Test
    void testExecuteStopSongWithNoSongBeingStreamed() throws UserNotLoggedException, NoSongCurrentlyStreamedException {
        Command command = CommandCreator.newCommand("stop");

        doThrow(new NoSongCurrentlyStreamedException("No song is currently streamed.")).when(spotify)
            .stopSong(clientKey);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: No song is currently streamed.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"stop\" and no song is being streamed.");
    }

    @Test
    void testExecuteStopSongWithValidData() throws UserNotLoggedException, NoSongCurrentlyStreamedException {
        Command command = CommandCreator.newCommand("stop");

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Playing stopped.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"stop\" with correct data.");

        verify(spotify, times(1)).stopSong(clientKey);
    }

    @Test
    void testExecuteSearchSongsWithUserNotLogged()
        throws UserNotLoggedException, NoSuchSongExistsException {
        String[] words = {"1", "2"};

        Command command = CommandCreator.newCommand(String.format("search %s %s", words[0], words[1]));

        doThrow(new UserNotLoggedException("User is not logged.")).when(spotify).searchSongs(clientKey, words);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: User is not logged.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"search\" and user is not logged in.");
    }

    @Test
    void testExecuteSearchSongsWithNoMatches()
        throws UserNotLoggedException, NoSuchSongExistsException {
        String[] words = {"1", "2"};

        Command command = CommandCreator.newCommand(String.format("search %s %s", words[0], words[1]));

        doThrow(new NoSuchSongExistsException("There were no songs found.")).when(spotify)
            .searchSongs(clientKey, words);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: There were no songs found.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"search\" and there are not matches.");
    }

    @Test
    void testExecuteSearchSongsWithValidData()
        throws UserNotLoggedException, NoSuchSongExistsException {
        String[] words = {"1", "2"};

        Command command = CommandCreator.newCommand(String.format("search %s %s", words[0], words[1]));

        when(spotify.searchSongs(clientKey, words)).thenReturn(List.of("song1", "song2"));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "The following songs found: " + String.join(", ", List.of("song1", "song2"));

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"search\" and there are matches.");
    }

    @Test
    void testExecuteSearchMostListenedSongsWithNotCorrectArgumentsCount() {
        Command command1 = CommandCreator.newCommand("top");
        String result1 = commandExecutor.execute(clientKey, command1);
        String expectedResult1 =
            "Invalid count of arguments: \"top\" expects 1 arguments. Example: \"top <number>\"";

        assertEquals(expectedResult1, result1,
            "execute(...) should return correct response when called with \"top\" and not correct argument count.");

        Command command2 = CommandCreator.newCommand("top 1 test");
        String result2 = commandExecutor.execute(clientKey, command2);
        String expectedResult2 =
            "Invalid count of arguments: \"top\" expects 1 arguments. Example: \"top <number>\"";

        assertEquals(expectedResult2, result2,
            "execute(...) should return correct response when called with \"top\" and not correct argument count.");
    }

    @Test
    void testExecuteSearchMostListenedSongsWithUserNotLogged()
        throws UserNotLoggedException {
        int number = 2;

        Command command = CommandCreator.newCommand(String.format("top %s", number));

        doThrow(new UserNotLoggedException("User is not logged.")).when(spotify)
            .searchMostListenedSongs(clientKey, number);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: User is not logged.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"top\" and user is not logged in.");
    }

    @Test
    void testExecuteSearchMostListenedWhenNumberIsNotInteger() {
        Command command = CommandCreator.newCommand("top test");

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: Value of number should be digit.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"top\" and number is not digit.");
    }

    @Test
    void testExecuteSearchMostListenedWhenNumberIsZero()
        throws UserNotLoggedException {
        int number = 0;

        Command command = CommandCreator.newCommand(String.format("top %s", number));

        when(spotify.searchMostListenedSongs(clientKey, number)).thenReturn(List.of());

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Top 0 songs: .";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"top\" and called with 0.");
    }

    @Test
    void testExecuteSearchMostListenedWhenNumberMoreThenZero()
        throws UserNotLoggedException {
        int number = 2;

        Command command = CommandCreator.newCommand(String.format("top %s", number));

        when(spotify.searchMostListenedSongs(clientKey, number)).thenReturn(List.of("song1", "song2"));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Top 2 songs: 1. song1, 2. song2.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"top\" and called with number more then zero.");
    }

    @Test
    void testExecuteShowPlaylistWithNotCorrectArgumentsCount() {
        Command command1 = CommandCreator.newCommand("show-playlist");
        String result1 = commandExecutor.execute(clientKey, command1);
        String expectedResult1 =
            "Invalid count of arguments: \"show-playlist\" expects 1 arguments. Example: \"show-playlist <name_of_the_playlist>\"";

        assertEquals(expectedResult1, result1,
            "execute(...) should return correct response when called with \"show-playlist\" and not correct argument count.");

        Command command2 = CommandCreator.newCommand("show-playlist playlist1 test");
        String result2 = commandExecutor.execute(clientKey, command2);
        String expectedResult2 =
            "Invalid count of arguments: \"show-playlist\" expects 1 arguments. Example: \"show-playlist <name_of_the_playlist>\"";

        assertEquals(expectedResult2, result2,
            "execute(...) should return correct response when called with \"show-playlist\" and not correct argument count.");
    }

    @Test
    void testExecuteShowPlaylistWithUserNotLogged()
        throws UserNotLoggedException, NoSuchPlaylistExistsException {
        String name = "playlist1";

        Command command = CommandCreator.newCommand(String.format("show-playlist %s", name));

        doThrow(new UserNotLoggedException("User is not logged.")).when(spotify)
            .getPlaylistByName(clientKey, name);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: User is not logged.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"show-playlist\" and user is not logged in.");
    }

    @Test
    void testExecuteShowPlaylistWithNotPlaylistExisting() throws UserNotLoggedException, NoSuchPlaylistExistsException {
        String name = "playlist1";

        Command command = CommandCreator.newCommand(String.format("show-playlist %s", name));

        doThrow(new NoSuchPlaylistExistsException("There is no playlist with that name.")).when(spotify)
            .getPlaylistByName(clientKey, name);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: There is no playlist with that name.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"show-playlist\" and playlist does not exist.");
    }

    @Test
    void testExecuteShowPlaylistWithEmptyPlaylist() throws UserNotLoggedException, NoSuchPlaylistExistsException {
        String name = "playlist1";

        Playlist playlist = new Playlist(name, "test1@test.test", new ArrayList<>());

        Command command = CommandCreator.newCommand(String.format("show-playlist %s", name));

        when(spotify.getPlaylistByName(clientKey, name)).thenReturn(playlist);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "The playlist \"playlist1\" made by \"test1@test.test\" is empty.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"show-playlist\" and playlist is empty.");
    }

    @Test
    void testExecuteShowPlaylistWithFilledPlaylist() throws UserNotLoggedException, NoSuchPlaylistExistsException {
        String name = "playlist1";

        Playlist playlist = new Playlist(name, "test1@test.test", new ArrayList<>(List.of("song1", "song2")));

        Command command = CommandCreator.newCommand(String.format("show-playlist %s", name));

        when(spotify.getPlaylistByName(clientKey, name)).thenReturn(playlist);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "The playlist \"playlist1\" made by \"test1@test.test\" contains the following songs: song1, song2.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"show-playlist\" and playlist is filled.");
    }

    @Test
    void testExecuteCreatePlaylistWithNotCorrectArgumentsCount() {
        Command command1 = CommandCreator.newCommand("create-playlist");
        String result1 = commandExecutor.execute(clientKey, command1);
        String expectedResult1 =
            "Invalid count of arguments: \"create-playlist\" expects 1 arguments. Example: \"create-playlist <name_of_the_playlist>\"";

        assertEquals(expectedResult1, result1,
            "execute(...) should return correct response when called with \"create-playlist\" and not correct argument count.");

        Command command2 = CommandCreator.newCommand("create-playlist playlist1 test");
        String result2 = commandExecutor.execute(clientKey, command2);
        String expectedResult2 =
            "Invalid count of arguments: \"create-playlist\" expects 1 arguments. Example: \"create-playlist <name_of_the_playlist>\"";

        assertEquals(expectedResult2, result2,
            "execute(...) should return correct response when called with \"create-playlist\" and not correct argument count.");
    }

    @Test
    void testExecuteCreatePlaylistWithUserNotLogged()
        throws UserNotLoggedException, SuchPlaylistAlreadyExistsException {
        String name = "playlist1";

        Command command = CommandCreator.newCommand(String.format("create-playlist %s", name));

        doThrow(new UserNotLoggedException("User is not logged.")).when(spotify)
            .createPlaylist(clientKey, name);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: User is not logged.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"create-playlist\" and user is not logged in.");
    }

    @Test
    void testExecuteCreatePlaylistWithSuchAlreadyExisting()
        throws UserNotLoggedException, SuchPlaylistAlreadyExistsException {
        String name = "playlist1";

        Command command = CommandCreator.newCommand(String.format("create-playlist %s", name));

        doThrow(new SuchPlaylistAlreadyExistsException("There is a playlist with that name already.")).when(spotify)
            .createPlaylist(clientKey, name);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: There is a playlist with that name already.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"create-playlist\" and playlist name that already exists.");
    }

    @Test
    void testExecuteCreatePlaylistWithValidData()
        throws UserNotLoggedException, SuchPlaylistAlreadyExistsException {
        String name = "playlist1";

        Command command = CommandCreator.newCommand(String.format("create-playlist %s", name));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Playlist \"playlist1\" was created.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"create-playlist\" with valid data.");

        verify(spotify, times(1)).createPlaylist(clientKey, name);
    }

    @Test
    void testExecuteAddSongToWithNotCorrectArgumentsCount() {
        Command command1 = CommandCreator.newCommand("add-song-to");
        String result1 = commandExecutor.execute(clientKey, command1);
        String expectedResult1 =
            "Invalid count of arguments: \"add-song-to\" expects 2 arguments. Example: \"add-song-to <name_of_the_playlist> <song>\"";

        assertEquals(expectedResult1, result1,
            "execute(...) should return correct response when called with \"add-song-to\" and not correct argument count.");

        Command command2 = CommandCreator.newCommand("add-song-to playlist1 song1 test");
        String result2 = commandExecutor.execute(clientKey, command2);
        String expectedResult2 =
            "Invalid count of arguments: \"add-song-to\" expects 2 arguments. Example: \"add-song-to <name_of_the_playlist> <song>\"";

        assertEquals(expectedResult2, result2,
            "execute(...) should return correct response when called with \"add-song-to\" and not correct argument count.");
    }

    @Test
    void testExecuteAddSongToWithUserNotLogged()
        throws UserNotLoggedException, NoAccessToPlaylistException,
        NoSuchSongExistsException, NoSuchPlaylistExistsException {
        String playlistName = "playlist1";
        String songTitle = "song1";

        Command command = CommandCreator.newCommand(String.format("add-song-to %s %s", playlistName, songTitle));

        doThrow(new UserNotLoggedException("User is not logged.")).when(spotify)
            .addSongTo(clientKey, playlistName, songTitle);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: User is not logged.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"add-song-to\" and user is not logged in.");
    }

    @Test
    void testExecuteAddSongToWithNonExistingPlaylist()
        throws UserNotLoggedException, NoAccessToPlaylistException, NoSuchSongExistsException,
        NoSuchPlaylistExistsException {
        String playlistName = "playlist1";
        String songTitle = "song1";

        Command command = CommandCreator.newCommand(String.format("add-song-to %s %s", playlistName, songTitle));

        doThrow(new NoSuchPlaylistExistsException("There is no playlist with that name.")).when(spotify)
            .addSongTo(clientKey, playlistName, songTitle);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: There is no playlist with that name.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"add-song-to\" and playlist does not exist.");
    }

    @Test
    void testExecuteAddSongToWithInvalidAccess()
        throws UserNotLoggedException, NoAccessToPlaylistException, NoSuchSongExistsException,
        NoSuchPlaylistExistsException {
        String playlistName = "playlist1";
        String songTitle = "song1";

        Command command = CommandCreator.newCommand(String.format("add-song-to %s %s", playlistName, songTitle));

        doThrow(new NoAccessToPlaylistException("The playlist do not belong to current user.")).when(spotify)
            .addSongTo(clientKey, playlistName, songTitle);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: The playlist do not belong to current user.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"add-song-to\" and playlist does not belong to user.");
    }

    @Test
    void testExecuteAddSongToWithNonExistingSong()
        throws UserNotLoggedException, NoAccessToPlaylistException, NoSuchSongExistsException,
        NoSuchPlaylistExistsException {
        String playlistName = "playlist1";
        String songTitle = "song1";

        Command command = CommandCreator.newCommand(String.format("add-song-to %s %s", playlistName, songTitle));

        doThrow(new NoSuchSongExistsException("There is no song with that title.")).when(spotify)
            .addSongTo(clientKey, playlistName, songTitle);

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Error: There is no song with that title.";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"add-song-to\" and song does not exist.");
    }

    @Test
    void testExecuteAddSongToWithValidData()
        throws UserNotLoggedException, NoAccessToPlaylistException, NoSuchSongExistsException,
        NoSuchPlaylistExistsException {
        String playlistName = "playlist1";
        String songTitle = "song1";

        Command command = CommandCreator.newCommand(String.format("add-song-to %s %s", playlistName, songTitle));

        String result = commandExecutor.execute(clientKey, command);

        String expectedResult = "Successfully added \"song1\" to \"playlist1\".";

        assertEquals(expectedResult, result,
            "execute(...) should return correct response when called with \"add-song-to\" and valid data.");

        verify(spotify, times(1)).addSongTo(clientKey, playlistName, songTitle);
    }
}
