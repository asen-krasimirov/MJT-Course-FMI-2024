package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;

import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SpaceScannerAPI {
    /**
     * Returns all missions in the dataset.
     * If there are no missions, return an empty collection.
     */
    Collection<Mission> getAllMissions();

    /**
     * Returns all missions in the dataset with a given status.
     * If there are no missions, return an empty collection.
     *
     * @param missionStatus the status of the missions
     * @throws IllegalArgumentException if missionStatus is null
     */
    Collection<Mission> getAllMissions(MissionStatus missionStatus);

    /**
     * Returns the company with the most successful missions in a given time period.
     * If there are no missions, return an empty string.
     *
     * @param from the inclusive beginning of the time frame
     * @param to   the inclusive end of the time frame
     * @throws IllegalArgumentException if from or to is null
     * @throws TimeFrameMismatchException if to is before from
     */
    String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to);

    /**
     * Groups missions by country.
     * If there are no missions, return an empty map.
     */
    Map<String, Collection<Mission>> getMissionsPerCountry();

    /**
     * Returns the top N least expensive missions, ordered from cheapest to more expensive.
     * If there are no missions, return an empty list.
     *
     * @param n             the number of missions to be returned
     * @param missionStatus the status of the missions
     * @param rocketStatus  the status of the rockets
     * @throws IllegalArgumentException if n is less than ot equal to 0, missionStatus or rocketStatus is null
     */
    List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus);

    /**
     * Returns the most desired location for missions per company.
     * If there are no missions, return an empty map.
     */
    Map<String, String> getMostDesiredLocationForMissionsPerCompany();

    /**
     * Returns the company mapped to its location with most successful missions.
     * If there are no missions, return an empty map.
     *
     * @param from the inclusive beginning of the time frame
     * @param to   the inclusive end of the time frame
     * @throws IllegalArgumentException if from or to is null
     * @throws TimeFrameMismatchException if to is before from
     */
    Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to);

    /**
     * Returns all rockets in the dataset.
     * If there are no rockets, return an empty collection.
     */
    Collection<Rocket> getAllRockets();

    /**
     * Returns the top N tallest rockets, in decreasing order.
     * If there are no rockets, return an empty list.
     *
     * @param n the number of rockets to be returned
     * @throws IllegalArgumentException if n is less than ot equal to 0
     */
    List<Rocket> getTopNTallestRockets(int n);

    /**
     * Returns a mapping of rockets (by name) to their respective wiki page (if present).
     * If there are no rockets, return an empty map.
     */
    Map<String, Optional<String>> getWikiPageForRocket();

    /**
     * Returns the wiki pages for the rockets used in the N most expensive missions.
     * If there are no missions, return an empty list.
     *
     * @param n             the number of missions to be returned
     * @param missionStatus the status of the missions
     * @param rocketStatus  the status of the rockets
     * @throws IllegalArgumentException if n is less than ot equal to 0, or missionStatus or rocketStatus is null
     */
    List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                   RocketStatus rocketStatus);

    /**
     * Saves the name of the most reliable rocket in a given time period in an encrypted format.
     *
     * @param outputStream the output stream where the encrypted result is written into
     * @param from         the inclusive beginning of the time frame
     * @param to           the inclusive end of the time frame
     * @throws IllegalArgumentException if outputStream, from or to is null
     * @throws CipherException if the encrypt/decrypt operation cannot be completed successfully
     * @throws TimeFrameMismatchException if to is before from
     */
    void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException;
}
