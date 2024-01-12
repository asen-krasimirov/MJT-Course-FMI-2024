package bg.sofia.uni.fmi.mjt.space.utils;

import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Collection;

public class Loader {
    public static void loadMissions(Reader missionsReader, Collection<Mission> missions) {
        try (BufferedReader bufferedReader = new BufferedReader(missionsReader)) {
            bufferedReader.readLine();    // headers clean up

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                missions.add(Mission.of(line));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Problem occurred with reading from missionsReader.", e);
        }
    }

    public static void loadRockets(Reader rocketsReader, Collection<Rocket> rockets) {
        try (BufferedReader bufferedReader = new BufferedReader(rocketsReader)) {
            bufferedReader.readLine();    // headers clean up

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                rockets.add(Rocket.of(line));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Problem occurred with reading from rocketsReader.", e);
        }
    }
}
