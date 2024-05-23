package bg.sofia.uni.fmi.mjt.spotify.database;

import bg.sofia.uni.fmi.mjt.spotify.exception.playlist.SuchPlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.song.Playlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PlaylistsDatabase {
    private final Set<Playlist> playlists;

    public PlaylistsDatabase() {
        playlists = new HashSet<>();
    }

    public void load(Reader infoReader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(infoReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            playlists.add(Playlist.of(line));
        }
    }

    public void save(Writer writer) throws IOException {
        for (Playlist playlist : playlists) {
            StringBuilder toWrite =
                new StringBuilder(playlist.name()).append(";").append(playlist.author()).append(";");

            for (int i = 0; i < playlist.songTitles().size(); ++i) {
                toWrite.append(playlist.songTitles().get(i));

                if (i != playlist.songTitles().size() - 1) {
                    toWrite.append(",");
                }
            }

            toWrite.append(System.lineSeparator());

            writer.write(toWrite.toString());
        }
    }

    public Playlist getPlaylistByName(String name) {
        return playlists
            .stream()
            .filter(song -> song.name().equals(name))
            .findFirst()
            .orElse(null);
    }

    public void createPlaylist(String name, String email) throws SuchPlaylistAlreadyExistsException {
        if (getPlaylistByName(name) != null) {
            throw new SuchPlaylistAlreadyExistsException("There is a playlist with that name already.");
        }

        Playlist newPlaylist = new Playlist(name, email, new ArrayList<>());

        playlists.add(newPlaylist);
    }
}
