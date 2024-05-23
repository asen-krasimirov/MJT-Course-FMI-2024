package bg.sofia.uni.fmi.mjt.spotify;

import static bg.sofia.uni.fmi.mjt.spotify.config.SpotifyConfig.PLAYLISTS_INFO_DATABASE_SOURCE;
import static bg.sofia.uni.fmi.mjt.spotify.config.SpotifyConfig.SONGS_DATA_DATABASE_SOURCE;
import static bg.sofia.uni.fmi.mjt.spotify.config.SpotifyConfig.SONGS_INFO_DATABASE_SOURCE;
import static bg.sofia.uni.fmi.mjt.spotify.config.SpotifyConfig.USERS_DATABASE_SOURCE;
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
import bg.sofia.uni.fmi.mjt.spotify.logger.ExceptionLogger;
import bg.sofia.uni.fmi.mjt.spotify.song.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.user.User;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.SelectionKey;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spotify implements SpotifyAPI {
    private final UsersDatabase usersDatabase;

    private final SongsDatabase songsDatabase;

    private final PlaylistsDatabase playlistsDatabase;

    private final Map<SelectionKey, User> users;

    public Spotify() {
        usersDatabase = new UsersDatabase();
        songsDatabase = new SongsDatabase();
        playlistsDatabase = new PlaylistsDatabase();

        users = new HashMap<>();
    }

    public Spotify(UsersDatabase usersDatabase, SongsDatabase songsDatabase, PlaylistsDatabase playlistsDatabase,
                   Map<SelectionKey, User> users) {
        this.usersDatabase = usersDatabase;
        this.songsDatabase = songsDatabase;
        this.playlistsDatabase = playlistsDatabase;

        this.users = users;
    }

    public void loadDatabases() {
        try (FileReader usersReader = new FileReader(USERS_DATABASE_SOURCE);
             FileReader songsReader = new FileReader(SONGS_INFO_DATABASE_SOURCE);
             FileReader playlistsReader = new FileReader(PLAYLISTS_INFO_DATABASE_SOURCE)) {
            usersDatabase.load(usersReader);

            songsDatabase.load(songsReader, Path.of(SONGS_DATA_DATABASE_SOURCE));

            playlistsDatabase.load(playlistsReader);
        } catch (IOException exception) {
            ExceptionLogger.logException(exception);
            throw new UncheckedIOException("A problem occurred with reading from file.", exception);
        } catch (UnsupportedAudioFileException exception) {
            ExceptionLogger.logException(exception);
            throw new RuntimeException("Unsupported audio file in server database.", exception);
        }
    }

    public void saveDatabases() {
        try (FileWriter usersWriter = new FileWriter(USERS_DATABASE_SOURCE);
             FileWriter songsWriter = new FileWriter(SONGS_INFO_DATABASE_SOURCE);
             FileWriter playlistsWriter = new FileWriter(PLAYLISTS_INFO_DATABASE_SOURCE)) {
            usersDatabase.save(usersWriter);

            songsDatabase.save(songsWriter);

            playlistsDatabase.save(playlistsWriter);
        } catch (IOException exception) {
            ExceptionLogger.logException(exception);
            throw new UncheckedIOException("A problem occurred with writing to file.", exception);
        }
    }

    public void registerUser(SelectionKey clientKey, String email, String password)
        throws UserAlreadyRegisteredException {
        User newUser = new User(email, password);

        usersDatabase.addUser(newUser);

        users.put(clientKey, newUser);
    }

    public void loginUser(SelectionKey clientKey, String email, String password)
        throws UserNotRegisteredException, WrongPasswordException {
        User user = usersDatabase.getUserByEmail(email);

        if (user == null) {
            throw new UserNotRegisteredException("No user with such email registered yet.");
        }

        if (!user.password().equals(password)) {
            throw new WrongPasswordException("Invalid password was provided.");
        }

        users.put(clientKey, user);
    }

    private void isUserLogged(SelectionKey clientKey) throws UserNotLoggedException {
        if (!users.containsKey(clientKey)) {
            throw new UserNotLoggedException("User is not logged.");
        }
    }

    public void disconnectUser(SelectionKey clientKey) throws UserNotLoggedException {
        isUserLogged(clientKey);
        users.remove(clientKey);
    }

    public void playSong(SelectionKey clientKey, String title)
        throws UserNotLoggedException, NoSuchSongExistsException {
        isUserLogged(clientKey);
        songsDatabase.playSong(clientKey, title);
    }

    public void stopSong(SelectionKey clientKey) throws UserNotLoggedException, NoSongCurrentlyStreamedException {
        isUserLogged(clientKey);
        songsDatabase.stopSong(clientKey);
    }

    public List<String> searchSongs(SelectionKey clientKey, String[] words)
        throws UserNotLoggedException, NoSuchSongExistsException {
        isUserLogged(clientKey);

        List<String> songTitles = songsDatabase.searchSongs(words);

        if (songTitles.isEmpty()) {
            throw new NoSuchSongExistsException("There were no songs found.");
        }

        return songTitles;
    }

    public List<String> searchMostListenedSongs(SelectionKey clientKey, int n) throws UserNotLoggedException {
        isUserLogged(clientKey);
        return songsDatabase.searchMostListenedSongs(n);
    }

    public Playlist getPlaylistByName(SelectionKey clientKey, String name)
        throws UserNotLoggedException, NoSuchPlaylistExistsException {
        isUserLogged(clientKey);

        Playlist playlist = playlistsDatabase.getPlaylistByName(name);

        if (playlist == null) {
            throw new NoSuchPlaylistExistsException("There is no playlist with that name.");
        }

        return playlist;
    }

    public void createPlaylist(SelectionKey clientKey, String name)
        throws UserNotLoggedException, SuchPlaylistAlreadyExistsException {
        isUserLogged(clientKey);

        User user = users.get(clientKey);

        playlistsDatabase.createPlaylist(name, user.email());
    }

    public void addSongTo(SelectionKey clientKey, String playlistName, String songTitle)
        throws UserNotLoggedException, NoSuchPlaylistExistsException, NoAccessToPlaylistException,
        NoSuchSongExistsException {
        isUserLogged(clientKey);

        User user = users.get(clientKey);

        Playlist playlist = playlistsDatabase.getPlaylistByName(playlistName);

        if (playlist == null) {
            throw new NoSuchPlaylistExistsException("There is no playlist with that name.");
        } else if (!playlist.author().equals(user.email())) {
            throw new NoAccessToPlaylistException("The playlist do not belong to current user.");
        } else if (songsDatabase.getSongByTitle(songTitle) == null) {
            throw new NoSuchSongExistsException("There is no song with that title.");
        }

        playlist.addSong(songTitle);
    }
}
