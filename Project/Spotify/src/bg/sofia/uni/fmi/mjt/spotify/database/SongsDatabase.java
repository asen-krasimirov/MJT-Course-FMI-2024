package bg.sofia.uni.fmi.mjt.spotify.database;

import bg.sofia.uni.fmi.mjt.spotify.exception.song.NoSongCurrentlyStreamedException;
import bg.sofia.uni.fmi.mjt.spotify.exception.song.NoSuchSongExistsException;
import bg.sofia.uni.fmi.mjt.spotify.song.Song;
import bg.sofia.uni.fmi.mjt.spotify.song.SongStreamThread;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.SelectionKey;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongsDatabase {
    private final List<Song> songs;

    private final Map<SelectionKey, SongStreamThread> playedSongs;

    public SongsDatabase() {
        songs = new ArrayList<>();
        playedSongs = new HashMap<>();
    }

    public void load(Reader infoReader, Path songsDir) throws IOException, UnsupportedAudioFileException {
        BufferedReader bufferedReader = new BufferedReader(infoReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            songs.add(Song.of(line));
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(songsDir, "*.wav")) {
            int index = 0;

            for (Path songFile : stream) {
                songs.get(index).setSongPath(songFile.toString());
                index++;
            }
        }
    }

    public void save(Writer writer) throws IOException {
        for (Song song : songs) {
            writer.write(song.getTitle() + ";" + song.getAuthor() + ";" + song.getPlays() + System.lineSeparator());
        }
    }

    public Song getSongByTitle(String title) {
        return songs
            .stream()
            .filter(song -> song.getTitle().equals(title))
            .findFirst()
            .orElse(null);
    }

    public void playSong(SelectionKey clientKey, String title) throws NoSuchSongExistsException {
        Song song = getSongByTitle(title);

        if (song == null) {
            throw new NoSuchSongExistsException("No song with such title exists.");
        }

        song.play();

        SongStreamThread songStreamThread = new SongStreamThread(clientKey, song);

        playedSongs.put(clientKey, songStreamThread);

        songStreamThread.start();
    }

    public void stopSong(SelectionKey clientKey) throws NoSongCurrentlyStreamedException {
        if (!playedSongs.containsKey(clientKey)) {
            throw new NoSongCurrentlyStreamedException("No song is currently streamed.");
        }

        playedSongs.get(clientKey).stopSong();
        playedSongs.remove(clientKey);
    }

    public List<String> searchSongs(String[] words) {
        return songs
            .stream()
            .filter(song -> song.isAMatch(words))
            .map(Song::getTitle)
            .toList();
    }

    public List<String> searchMostListenedSongs(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Value of n should not be negative.");
        }

        return songs
            .stream()
            .sorted((song1, song2) -> Integer.compare(song2.getPlays(), song1.getPlays()))
            .limit(n)
            .map(Song::getTitle)
            .toList();
    }
}
