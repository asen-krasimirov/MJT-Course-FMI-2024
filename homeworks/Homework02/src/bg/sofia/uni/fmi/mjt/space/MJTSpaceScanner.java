package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.algorithm.SymmetricBlockCipher;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import bg.sofia.uni.fmi.mjt.space.utils.Loader;
import static java.util.stream.Collectors.reducing;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MJTSpaceScanner implements SpaceScannerAPI {
    private final SymmetricBlockCipher symmetricBlockCipher;
    private final List<Mission> missions;
    private final List<Rocket> rockets;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        this.symmetricBlockCipher = new Rijndael(secretKey);
        this.missions = new ArrayList<>();
        this.rockets = new ArrayList<>();

        Loader.loadMissions(missionsReader, missions);
        Loader.loadRockets(rocketsReader, rockets);
    }

    @Override
    public Collection<Mission> getAllMissions() {
        return missions
            .stream()
            .toList();
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        if (missionStatus == null) {
            throw new IllegalArgumentException("Value of missionStatus should not be null!");
        }

        return missions
            .stream()
            .filter(mission -> mission.missionStatus().equals(missionStatus))
            .toList();
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Values of from and to should not be null!");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("To is before from!");
        }

        Map<String, Long> missionsByCompany = missions
            .stream()
            .filter(mission -> !(mission.date().isBefore(from) || mission.date().isAfter(to)))
            .filter(mission -> mission.missionStatus().equals(MissionStatus.SUCCESS))
            .collect(Collectors.groupingBy(Mission::company, Collectors.counting()));

        Map.Entry<String, Long> mostSuccessfulCompany = missionsByCompany
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .orElse(null);

        if (mostSuccessfulCompany == null) {
            return "";
        } else {
            return mostSuccessfulCompany.getKey();
        }
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions
            .stream()
            .collect(Collectors.groupingBy(Mission::getCountry, Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("Value of n should not be less then or equal to 0!");
        }

        if (missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("Values of missionStatus and rocketStatus should not be null!");
        }

        return missions
            .stream()
            .filter(mission -> mission.missionStatus().equals(missionStatus)
                && mission.rocketStatus().equals(rocketStatus))
            .filter(mission -> mission.cost().isPresent())
            .sorted(Comparator.comparing(mission -> mission.cost().get()))
            .limit(n)
            .toList();
    }

    private String getMostCommonLocationForCountry(List<String> locations) {
        return locations
            .stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseGet(() -> "");
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        Map<String, List<String>> locationsPerCompany = missions
            .stream()
            .collect(
                Collectors.groupingBy(Mission::company, Collectors.mapping(Mission::location, Collectors.toList())));

        return locationsPerCompany
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> getMostCommonLocationForCountry(e.getValue())
            ));
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Values of from and to should not be null!");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("To is before from!");
        }

        Map<String, List<String>> successfulLocationsPerCompany = missions
            .stream()
            .filter(mission -> !(mission.date().isBefore(from) || mission.date().isAfter(to)))
            .filter(mission -> mission.missionStatus().equals(MissionStatus.SUCCESS))
            .collect(
                Collectors.groupingBy(Mission::company, Collectors.mapping(Mission::location, Collectors.toList())));

        return successfulLocationsPerCompany
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> getMostCommonLocationForCountry(e.getValue())
            ));
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return rockets
            .stream()
            .toList();
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Value of n should not bet less then or equal to zero!");
        }

        return rockets
            .stream()
            .filter(rocket -> rocket.height().isPresent())
            .sorted((rocket1, rocket2) -> Double.compare(rocket2.height().get(), rocket1.height().get()))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets
            .stream()
            .collect(Collectors.toMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("Value of n should not be less then or equal to 0!");
        }

        if (missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("Values of missionStatus and rocketStatus should not be null!");
        }

        Map<String, String> wikiByRocketName = rockets
            .stream()
            .filter(rocket -> rocket.wiki().isPresent())
            .collect(Collectors.toMap(Rocket::name, rocket -> rocket.wiki().get()));

        return missions
            .stream()
            .filter(mission -> mission.missionStatus().equals(missionStatus)
                && mission.rocketStatus().equals(rocketStatus))
            .filter(mission -> mission.cost().isPresent())
            .sorted((mission1, mission2) -> Double.compare(mission2.cost().get(), mission1.cost().get()))
            .limit(n)
            .map(mission -> wikiByRocketName.get(mission.detail().rocketName()))
            .toList();

    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        if (outputStream == null || from == null || to == null) {
            throw new IllegalArgumentException("Values of outputStream, from and to should not be null!");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("To is before from!");
        }

        String mostReliableRocketName = findMostReliableRocket(from, to);

        encryptRocketNameInOutputStream(outputStream, mostReliableRocketName);
    }

    private String findMostReliableRocket(LocalDate from, LocalDate to) {
        Map<String, Double> missionsCount = missions
            .stream()
            .filter(mission -> !(mission.date().isBefore(from) || mission.date().isAfter(to)))
            .collect(
                Collectors.groupingBy(mission -> mission.detail().rocketName(), reducing(0.0, e -> 1.0, Double::sum)));

        Map<String, Double> successfulMissionsCount = missions
            .stream()
            .filter(mission -> !(mission.date().isBefore(from) || mission.date().isAfter(to)))
            .filter(mission -> mission.missionStatus().equals(MissionStatus.SUCCESS))
            .collect(
                Collectors.groupingBy(mission -> mission.detail().rocketName(), reducing(0.0, e -> 1.0, Double::sum)));

        Map<String, Double> failureMissionsCount = missions
            .stream()
            .filter(mission -> !(mission.date().isBefore(from) || mission.date().isAfter(to)))
            .filter(mission -> !mission.missionStatus().equals(MissionStatus.SUCCESS))
            .collect(
                Collectors.groupingBy(mission -> mission.detail().rocketName(), reducing(0.0, e -> 1.0, Double::sum)));

        return calculateMostReliableRocket(missionsCount, successfulMissionsCount, failureMissionsCount);
    }

    private String calculateMostReliableRocket(Map<String, Double> missionsCount,
                                               Map<String, Double> successfulMissionsCount,
                                               Map<String, Double> failureMissionsCount) {
        String mostReliableRocketName = "";
        double bestReliability = 0.0;

        for (var entry : missionsCount.entrySet()) {
            double currentReliability = (2.0 * successfulMissionsCount.getOrDefault(entry.getKey(), 0.0) +
                failureMissionsCount.getOrDefault(entry.getKey(), 0.0)) / (2.0 * entry.getValue());

            if (currentReliability > bestReliability) {
                mostReliableRocketName = entry.getKey();
                bestReliability = currentReliability;
            }
        }

        return mostReliableRocketName;
    }

    private void encryptRocketNameInOutputStream(OutputStream outputStream, String mostReliableRocketName)
        throws CipherException {
        InputStream inputStream = new ByteArrayInputStream(mostReliableRocketName.getBytes());
        symmetricBlockCipher.encrypt(inputStream, outputStream);
    }
}
