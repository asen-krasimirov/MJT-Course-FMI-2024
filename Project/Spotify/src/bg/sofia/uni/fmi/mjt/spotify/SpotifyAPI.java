package bg.sofia.uni.fmi.mjt.spotify;

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

import java.nio.channels.SelectionKey;
import java.util.List;

public interface SpotifyAPI {
    /**
     * Load all databases from files
     */
    void loadDatabases();

    /**
     * Save all data from databases into files
     */
    void saveDatabases();

    /**
     * Register client into the system
     *
     * @param clientKey the SelectionKey of the client
     * @param email the email to register with
     * @param password the password to register with
     *
     * @throws UserAlreadyRegisteredException if the user is already registered
     */
    void registerUser(SelectionKey clientKey, String email, String password) throws UserAlreadyRegisteredException;

    /**
     * Log client into the system
     *
     * @param clientKey the SelectionKey of the client
     * @param email the email to log in with
     * @param password the password to log in with
     *
     * @throws UserNotRegisteredException if the user is not yet registered
     * @throws WrongPasswordException if the provided password is not matching the one in the database
     */
    void loginUser(SelectionKey clientKey, String email, String password)
        throws UserNotRegisteredException, WrongPasswordException;

    /**
     * Disconnect client from the system
     *
     * @param clientKey the SelectionKey of the client
     *
     * @throws UserNotLoggedException if the user is not logged in
     */
    void disconnectUser(SelectionKey clientKey) throws UserNotLoggedException;

    /**
     * Stream song to user
     *
     * @param clientKey the SelectionKey of the client
     * @param title the title of the song to stream
     *
     * @throws UserNotLoggedException if the user is not logged in
     * @throws NoSuchSongExistsException if the song does not exist
     */
    void playSong(SelectionKey clientKey, String title)
        throws UserNotLoggedException, NoSuchSongExistsException;

    /**
     * Stop streaming song to user
     *
     * @param clientKey the SelectionKey of the client
     *
     * @throws UserNotLoggedException if the user is not logged in
     * @throws NoSongCurrentlyStreamedException if no song is being streamed
     */
    void stopSong(SelectionKey clientKey) throws UserNotLoggedException, NoSongCurrentlyStreamedException;

    /**
     * Search songs by provided keywords
     *
     * @param clientKey the SelectionKey of the client
     * @param words the words by which to search
     *
     * @throws UserNotLoggedException if the user is not logged in
     * @throws NoSuchSongExistsException if no song matching words is found
     */
    List<String> searchSongs(SelectionKey clientKey, String[] words)
        throws UserNotLoggedException, NoSuchSongExistsException;

    /**
     * Search top most played songs by provided number
     *
     * @param clientKey the SelectionKey of the client
     * @param n the top n song titles to be returned
     *
     * @throws UserNotLoggedException if the user is not logged in
     * @throws IllegalArgumentException is the value of n is negative
     */
    List<String> searchMostListenedSongs(SelectionKey clientKey, int n) throws UserNotLoggedException;

    /**
     * Get playlist by name
     *
     * @param clientKey the SelectionKey of the client
     * @param name the name of the playlist to be returned
     *
     * @throws UserNotLoggedException if the user is not logged in
     * @throws NoSuchPlaylistExistsException if the name does not match any playlist
     */
    Playlist getPlaylistByName(SelectionKey clientKey, String name)
        throws UserNotLoggedException, NoSuchPlaylistExistsException;

    /**
     * Create playlist by name
     *
     * @param clientKey the SelectionKey of the client
     * @param name the name of the playlist to be created
     *
     * @throws UserNotLoggedException if the user is not logged in
     * @throws SuchPlaylistAlreadyExistsException if the name already matches other playlist
     */
    void createPlaylist(SelectionKey clientKey, String name)
        throws UserNotLoggedException, SuchPlaylistAlreadyExistsException;

    /**
     * Adds song to playlist
     *
     * @param clientKey the SelectionKey of the client
     * @param playlistName the name of the playlist
     * @param songTitle the name of the song
     *
     * @throws UserNotLoggedException if the user is not logged in
     * @throws NoSuchPlaylistExistsException if the playlistName does not match any playlist
     * @throws NoAccessToPlaylistException if the client does not own the playlist
     * @throws NoSuchSongExistsException if the songTitle does not match any song
     */
    void addSongTo(SelectionKey clientKey, String playlistName, String songTitle)
        throws UserNotLoggedException, NoSuchPlaylistExistsException, NoAccessToPlaylistException,
        NoSuchSongExistsException;
}
