package bg.sofia.uni.fmi.mjt.spotify.command;

import bg.sofia.uni.fmi.mjt.spotify.SpotifyAPI;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.AuthenticationException;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.UserNotLoggedException;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.UserNotRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.WrongEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.exception.authentication.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.NoAccessToPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.NoSuchPlaylistExistsException;
import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.PlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.exception.song.NoSongCurrentlyStreamedException;
import bg.sofia.uni.fmi.mjt.spotify.exception.song.NoSuchSongExistsException;
import bg.sofia.uni.fmi.mjt.spotify.exception.song.SongException;
import bg.sofia.uni.fmi.mjt.spotify.logger.ExceptionLogger;
import bg.sofia.uni.fmi.mjt.spotify.song.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.user.PasswordHash;
import bg.sofia.uni.fmi.mjt.spotify.validator.EmailValidator;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.List;

public class CommandExecutor {
    private static final String INVALID_ARGS_COUNT_MESSAGE_FORMAT =
        "Invalid count of arguments: \"%s\" expects %d arguments. Example: \"%s\"";

    private static final String SUCCESSFUL_REGISTRATION_MESSAGE =
        "You successfully registered.";

    private static final String SUCCESSFUL_LOGIN_MESSAGE =
        "You successfully logged in.";

    private static final String SUCCESSFUL_DISCONNECTED_MESSAGE =
        "You successfully disconnected.";

    private static final String SUCCESSFUL_PLAY_SONG_MESSAGE =
        "Now playing: ";

    private static final String SUCCESSFUL_STOP_SONG_MESSAGE =
        "Playing stopped.";

    private static final String SUCCESSFUL_SEARCH_MESSAGE =
        "The following songs found: ";

    private static final String SUCCESSFUL_TOP_MESSAGE =
        "Top %s songs: ";

    private static final String SUCCESSFUL_SHOW_PLAYLIST_MESSAGE =
        "The playlist \"%s\" made by \"%s\" contains the following songs: ";

    private static final String SUCCESSFUL_SHOW_EMPTY_PLAYLIST_MESSAGE =
        "The playlist \"%s\" made by \"%s\" is empty.";

    private static final String SUCCESSFUL_CREATE_PLAYLIST_MESSAGE =
        "Playlist \"%s\" was created.";

    private static final String SUCCESSFUL_ADD_TO_PLAYLIST_MESSAGE =
        "Successfully added \"%s\" to \"%s\".";

    private static final String REGISTER = "register";

    private static final String LOGIN = "login";

    private static final String DISCONNECT = "disconnect";

    private static final String PLAY_SONG = "play";

    private static final String STOP_SONG = "stop";

    private static final String SEARCH = "search";

    private static final String TOP = "top";

    private static final String SHOW_PLAYLIST = "show-playlist";

    private static final String CREATE_PLAYLIST = "create-playlist";

    private static final String ADD_SONG_TO = "add-song-to";

    private final SpotifyAPI spotify;

    public CommandExecutor(SpotifyAPI spotify) {
        this.spotify = spotify;
    }

    public String execute(SelectionKey clientKey, Command cmd) {
        try {
            return switch (cmd.command()) {
                case REGISTER -> registerUser(clientKey, cmd.arguments());
                case LOGIN -> loginUser(clientKey, cmd.arguments());
                case DISCONNECT -> disconnectUser(clientKey);
                case PLAY_SONG -> playSong(clientKey, cmd.arguments());
                case STOP_SONG -> stopSong(clientKey);
                case SEARCH -> searchSongs(clientKey, cmd.arguments());
                case TOP -> searchMostListenedSongs(clientKey, cmd.arguments());
                case SHOW_PLAYLIST -> showPlaylist(clientKey, cmd.arguments());
                case CREATE_PLAYLIST -> createPlaylist(clientKey, cmd.arguments());
                case ADD_SONG_TO -> addSongTo(clientKey, cmd.arguments());
                default -> "Unknown command";
            };
        } catch (AuthenticationException | SongException | PlaylistException | IllegalArgumentException exception) {
            return "Error: " + exception.getMessage();
        } catch (IOException exception) {
            ExceptionLogger.logException(exception, cmd.command() + " " + String.join(" ", cmd.arguments()));
            return "Unable to connect to the server." +
                "Try again later or contact administrator by providing the logs in logs.txt";
        }
    }

    private String registerUser(SelectionKey clientKey, String[] args)
        throws UserAlreadyRegisteredException, WrongEmailFormatException, IOException {
        if (args.length != 2) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, REGISTER, 2, REGISTER + " <email> <password>");
        }

        String email = args[0];

        if (!EmailValidator.isValidEmail(email)) {
            throw new WrongEmailFormatException("Email is not the correct format.");
        }

        String password = PasswordHash.hashPassword(args[1]);

        spotify.registerUser(clientKey, email, password);

        return SUCCESSFUL_REGISTRATION_MESSAGE;
    }

    private String loginUser(SelectionKey clientKey, String[] args) throws UserNotRegisteredException,
        WrongPasswordException {
        if (args.length != 2) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, LOGIN, 2, LOGIN + " <email> <password>");
        }

        String email = args[0];
        String password = PasswordHash.hashPassword(args[1]);

        spotify.loginUser(clientKey, email, password);

        return SUCCESSFUL_LOGIN_MESSAGE;
    }

    private String disconnectUser(SelectionKey clientKey) throws UserNotLoggedException {
        spotify.disconnectUser(clientKey);
        return SUCCESSFUL_DISCONNECTED_MESSAGE;
    }

    private String playSong(SelectionKey clientKey, String[] args)
        throws UserNotLoggedException, NoSuchSongExistsException {
        if (args.length != 1) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, PLAY_SONG, 1, PLAY_SONG + " <song>");
        }

        String title = args[0];

        spotify.playSong(clientKey, title);

        return SUCCESSFUL_PLAY_SONG_MESSAGE + title;
    }

    private String stopSong(SelectionKey clientKey) throws UserNotLoggedException, NoSongCurrentlyStreamedException {
        spotify.stopSong(clientKey);
        return SUCCESSFUL_STOP_SONG_MESSAGE;
    }

    private String searchSongs(SelectionKey clientKey, String[] args)
        throws UserNotLoggedException, NoSuchSongExistsException {
        List<String> songTitles = spotify.searchSongs(clientKey, args);
        return SUCCESSFUL_SEARCH_MESSAGE + String.join(", ", songTitles);
    }

    public static boolean isInputNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private String searchMostListenedSongs(SelectionKey clientKey, String[] args) throws UserNotLoggedException {
        if (args.length != 1) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, TOP, 1, TOP + " <number>");
        }

        if (!isInputNumeric(args[0])) {
            throw new IllegalArgumentException("Value of number should be digit.");
        }

        int number = Integer.parseInt(args[0]);

        List<String> songTitles = spotify.searchMostListenedSongs(clientKey, number);

        StringBuilder responseMessage = new StringBuilder(String.format(SUCCESSFUL_TOP_MESSAGE, number));

        for (int i = 0; i < songTitles.size(); ++i) {
            responseMessage.append(i + 1).append(". ").append(songTitles.get(i));

            if (i != songTitles.size() - 1) {
                responseMessage.append(", ");
            }
        }

        return responseMessage.append(".").toString();
    }

    private String showPlaylist(SelectionKey clientKey, String[] args)
        throws UserNotLoggedException, NoSuchPlaylistExistsException {
        if (args.length != 1) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, SHOW_PLAYLIST, 1,
                SHOW_PLAYLIST + " <name_of_the_playlist>");
        }

        String name = args[0];

        Playlist playlist = spotify.getPlaylistByName(clientKey, name);

        if (playlist.songTitles().isEmpty()) {
            return String.format(SUCCESSFUL_SHOW_EMPTY_PLAYLIST_MESSAGE, playlist.name(), playlist.author());
        } else {
            return String.format(SUCCESSFUL_SHOW_PLAYLIST_MESSAGE, playlist.name(), playlist.author()) +
                String.join(", ", playlist.songTitles()) + ".";
        }
    }

    private String createPlaylist(SelectionKey clientKey, String[] args)
        throws UserNotLoggedException, SuchPlaylistAlreadyExistsException {
        if (args.length != 1) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, CREATE_PLAYLIST, 1,
                CREATE_PLAYLIST + " <name_of_the_playlist>");
        }

        String name = args[0];

        spotify.createPlaylist(clientKey, name);

        return String.format(SUCCESSFUL_CREATE_PLAYLIST_MESSAGE, name);
    }

    private String addSongTo(SelectionKey clientKey, String[] args)
        throws UserNotLoggedException, NoAccessToPlaylistException, NoSuchSongExistsException,
        NoSuchPlaylistExistsException {
        if (args.length != 2) {
            return String.format(INVALID_ARGS_COUNT_MESSAGE_FORMAT, ADD_SONG_TO, 2,
                ADD_SONG_TO + " <name_of_the_playlist> <song>");
        }

        String playlistName = args[0];
        String songTitle = args[1];

        spotify.addSongTo(clientKey, playlistName, songTitle);

        return String.format(SUCCESSFUL_ADD_TO_PLAYLIST_MESSAGE, songTitle, playlistName);
    }
}
