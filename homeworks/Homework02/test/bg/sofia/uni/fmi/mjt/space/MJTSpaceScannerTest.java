package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MJTSpaceScannerTest {

    private static List<Mission> missionsTestResults;
    private static List<Rocket> rocketsTestResults;
    private static MJTSpaceScanner mjtSpaceScanner;
    private static MJTSpaceScanner emptyMjtSpaceScanner;

    static void loadMissionsTestResults() {
        Mission mission1 = Mission.of(
            "0,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\",\"Fri Aug 07, 2020\",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,\"50.0 \",Success"
        );

        Mission mission2 = Mission.of(
            "1,CASC,\"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China\",\"Thu Aug 06, 2020\",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,\"29.75 \",Success"
        );

        Mission mission3 = Mission.of(
            "2,SpaceX,\"Pad A, Boca Chica, Texas, USA\",\"Tue Aug 04, 2020\",Starship Prototype | 150 Meter Hop,StatusActive,,Success"
        );

        Mission mission4 = Mission.of(
            "3,Roscosmos,\"Site 200/39, Baikonur Cosmodrome, Kazakhstan\",\"Thu Jul 30, 2020\",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,\"65.0 \",Success"
        );

        Mission mission5 = Mission.of(
            "448,Roscosmos,\"Site 31/6, Baikonur Cosmodrome, Kazakhstan\",\"Tue Mar 29, 2016\",Soyuz 2.1a | Progress MS-02,StatusActive,\"48.5 \",Success"
        );

        Mission mission6 = Mission.of(
            "449,Roscosmos,\"Site 31/6, Baikonur Cosmodrome, Kazakhstan\",\"Wed Mar 30, 2016\",Soyuz 2.1a | Progress MS-02,StatusActive,\"48.5 \",Failure"
        );

        Mission mission7 = Mission.of(
            "450,Roscosmos,\"Site 31/6, Baikonur Cosmodrome, Kazakhstan\",\"Thu Mar 31, 2016\",Soyuz 2.1a | Progress MS-02,StatusActive,\"48.5 \",Success"
        );

        Mission mission8 = Mission.of(
            "2033,RVSN USSR,\"Site 16/2, Plesetsk Cosmodrome, Russia\",\"Thu Nov 20, 1986\",Molniya-M /Block 2BL | Cosmos 1793,StatusRetired,,Success"
        );

        Mission mission9 = Mission.of(
            "2034,RVSN USSR,\"Site 16/2, Plesetsk Cosmodrome, Russia\",\"Fri Nov 21, 1986\",Molniya-M /Block 2BL | Cosmos 1793,StatusRetired,,Partial Failure"
        );

        Mission mission10 = Mission.of(
            "2035,RVSN USSR,\"Site 16/2, Plesetsk Cosmodrome, Russia\",\"Sat Nov 22, 1986\",Molniya-M /Block 2BL | Cosmos 1793,StatusRetired,,Prelaunch Failure"
        );

        missionsTestResults =
            List.of(mission1, mission2, mission3, mission4, mission5, mission6, mission7, mission8, mission9,
                mission10);
    }

    static void loadRocketsTestResults() {
        Rocket rocket1 = Rocket.of(
            "0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m"
        );

        Rocket rocket2 = Rocket.of(
            "1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m"
        );

        Rocket rocket3 = Rocket.of(
            "2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m"
        );

        Rocket rocket4 = Rocket.of(
            "3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m"
        );

        Rocket rocket5 = Rocket.of(
            "120,Delta A,https://en.wikipedia.org/wiki/Delta_A,"
        );

        Rocket rocket6 = Rocket.of(
            "278,Poliot,,"
        );

        Rocket rocket7 = Rocket.of(
            "169,Falcon 9 Block 5,https://en.wikipedia.org/wiki/Falcon_9,70.0 m"
        );

        Rocket rocket8 = Rocket.of(
            "213,Long March 2D,https://en.wikipedia.org/wiki/Long_March_2D,41.06 m"
        );

        Rocket rocket9 = Rocket.of(
            "371,Starship Prototype,https://en.wikipedia.org/wiki/SpaceX_Starship,50.0 m"
        );

        Rocket rocket10 = Rocket.of(
            "294,Proton-M/Briz-M,https://en.wikipedia.org/wiki/Proton-M,58.2 m"
        );

        Rocket rocket11 = Rocket.of(
            "337,Soyuz 2.1a,https://en.wikipedia.org/wiki/Soyuz-2,"
        );

        Rocket rocket12 = Rocket.of(
            "247,Molniya-M /Block 2BL,https://en.wikipedia.org/wiki/Molniya-M,43.4 m"
        );

        rocketsTestResults = List.of(rocket1, rocket2, rocket3, rocket4, rocket5, rocket6, rocket7, rocket8, rocket9,
            rocket10, rocket11, rocket12);
    }

    private static SecretKey loadSecretKey() {
        byte[] keyBytes = "q0v2jGG99Ox+11srKkaolw==".getBytes();
        return new SecretKeySpec(keyBytes, "AES");
    }

    static void initializeMJTSpaceScanner() {
        StringBuilder missionsInfo = new StringBuilder();

        missionsInfo.append("Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket,\" Rocket\",Status Mission");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "0,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\",\"Fri Aug 07, 2020\",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,\"50.0 \",Success");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "1,CASC,\"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China\",\"Thu Aug 06, 2020\",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,\"29.75 \",Success");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "2,SpaceX,\"Pad A, Boca Chica, Texas, USA\",\"Tue Aug 04, 2020\",Starship Prototype | 150 Meter Hop,StatusActive,,Success");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "3,Roscosmos,\"Site 200/39, Baikonur Cosmodrome, Kazakhstan\",\"Thu Jul 30, 2020\",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,\"65.0 \",Success");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "448,Roscosmos,\"Site 31/6, Baikonur Cosmodrome, Kazakhstan\",\"Tue Mar 29, 2016\",Soyuz 2.1a | Progress MS-02,StatusActive,\"48.5 \",Success");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "449,Roscosmos,\"Site 31/6, Baikonur Cosmodrome, Kazakhstan\",\"Wed Mar 30, 2016\",Soyuz 2.1a | Progress MS-02,StatusActive,\"48.5 \",Failure");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "450,Roscosmos,\"Site 31/6, Baikonur Cosmodrome, Kazakhstan\",\"Thu Mar 31, 2016\",Soyuz 2.1a | Progress MS-02,StatusActive,\"48.5 \",Success");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "2033,RVSN USSR,\"Site 16/2, Plesetsk Cosmodrome, Russia\",\"Thu Nov 20, 1986\",Molniya-M /Block 2BL | Cosmos 1793,StatusRetired,,Success");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "2034,RVSN USSR,\"Site 16/2, Plesetsk Cosmodrome, Russia\",\"Fri Nov 21, 1986\",Molniya-M /Block 2BL | Cosmos 1793,StatusRetired,,Partial Failure");
        missionsInfo.append(System.lineSeparator());
        missionsInfo.append(
            "2035,RVSN USSR,\"Site 16/2, Plesetsk Cosmodrome, Russia\",\"Sat Nov 22, 1986\",Molniya-M /Block 2BL | Cosmos 1793,StatusRetired,,Prelaunch Failure");

        StringBuilder rocketsInfo = new StringBuilder();

        rocketsInfo.append("\"\",Name,Wiki,Rocket Height");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("120,Delta A,https://en.wikipedia.org/wiki/Delta_A,");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("278,Poliot,,");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("169,Falcon 9 Block 5,https://en.wikipedia.org/wiki/Falcon_9,70.0 m");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("213,Long March 2D,https://en.wikipedia.org/wiki/Long_March_2D,41.06 m");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("371,Starship Prototype,https://en.wikipedia.org/wiki/SpaceX_Starship,50.0 m");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("294,Proton-M/Briz-M,https://en.wikipedia.org/wiki/Proton-M,58.2 m");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("337,Soyuz 2.1a,https://en.wikipedia.org/wiki/Soyuz-2,");
        rocketsInfo.append(System.lineSeparator());
        rocketsInfo.append("247,Molniya-M /Block 2BL,https://en.wikipedia.org/wiki/Molniya-M,43.4 m");

        StringReader missionsReader = new StringReader(missionsInfo.toString());
        StringReader rocketsReader = new StringReader(rocketsInfo.toString());

        SecretKey secretKey = loadSecretKey();

        mjtSpaceScanner = new MJTSpaceScanner(missionsReader, rocketsReader, secretKey);
    }

    static void initializeEmptyMJTSpaceScanner() {
        StringReader emptyMissionsReader = new StringReader("");
        StringReader emptyRocketsReader = new StringReader("");
        SecretKey secretKey = loadSecretKey();

        emptyMjtSpaceScanner = new MJTSpaceScanner(emptyMissionsReader, emptyRocketsReader, secretKey);
    }

    @BeforeAll
    static void setUpTestCase() {
        loadMissionsTestResults();
        loadRocketsTestResults();
        initializeMJTSpaceScanner();
        initializeEmptyMJTSpaceScanner();
    }

    @Test
    void testGetAllMissionsInFilledDataset() {
        Collection<Mission> result = mjtSpaceScanner.getAllMissions();

        Assertions.assertIterableEquals(missionsTestResults, result, "getAllMissions() should return correct collection.");
    }

    @Test
    void testGetAllMissionsInEmptyDataset() {
        Collection<Mission> result = emptyMjtSpaceScanner.getAllMissions();

        Assertions.assertIterableEquals(List.of(), result, "getAllMissions() should return empty collection.");
    }

    @Test
    void testGetAllMissionsWithNullStatus() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> mjtSpaceScanner.getAllMissions(null),
            "getAllMissions(...) should throw IllegalArgumentException when called with null.");
    }

    @Test
    void testGetAllMissionsWithCorrectMissionType() {
        Collection<Mission> expectedResult =
            List.of(missionsTestResults.get(0), missionsTestResults.get(1), missionsTestResults.get(2),
                missionsTestResults.get(3), missionsTestResults.get(4), missionsTestResults.get(6),
                missionsTestResults.get(7));

        Collection<Mission> result = mjtSpaceScanner.getAllMissions(MissionStatus.SUCCESS);

        Assertions.assertIterableEquals(expectedResult, result, "getAllMissions(...) should return correct collection.");
    }

    @Test
    void testGetAllMissionsWithCorrectMissionTypeInEmptyDataset() {
        Collection<Mission> result = emptyMjtSpaceScanner.getAllMissions(MissionStatus.SUCCESS);

        Assertions.assertIterableEquals(List.of(), result, "getAllMissions(...) should return empty collection.");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullValues() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getCompanyWithMostSuccessfulMissions(null, LocalDate.now()),
            "getCompanyWithMostSuccessfulMissions(...) should throw IllegalArgumentException when called with null.");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getCompanyWithMostSuccessfulMissions(LocalDate.now(), null),
            "getCompanyWithMostSuccessfulMissions(...) should throw IllegalArgumentException when called with null.");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithInvalidTimes() {
        LocalDate startDate = LocalDate.of(2023, 12, 25);
        LocalDate endDate = LocalDate.of(2023, 12, 20);

        Assertions.assertThrows(TimeFrameMismatchException.class,
            () -> mjtSpaceScanner.getCompanyWithMostSuccessfulMissions(startDate, endDate),
            "getCompanyWithMostSuccessfulMissions(...) should throw TimeFrameMismatchException when called with invalid dates.");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithCorrectDate() {
        LocalDate startDate = LocalDate.of(1986, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);

        String result = mjtSpaceScanner.getCompanyWithMostSuccessfulMissions(startDate, endDate);

        Assertions.assertEquals("Roscosmos", result,
            "getCompanyWithMostSuccessfulMissions(...) should return correct company name.");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithCorrectDateAndEmptyDataset() {
        LocalDate startDate = LocalDate.of(1986, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);

        String result = emptyMjtSpaceScanner.getCompanyWithMostSuccessfulMissions(startDate, endDate);

        Assertions.assertEquals("", result,
            "getCompanyWithMostSuccessfulMissions(...) should return empty company name.");
    }

    @Test
    void testGetMissionsPerCountryWithFilledDataset() {
        Map<String, Collection<Mission>> expectedResult = Map.of(
            "USA", List.of(missionsTestResults.get(0), missionsTestResults.get(2)),
            "China", List.of(missionsTestResults.get(1)),
            "Kazakhstan", List.of(missionsTestResults.get(3), missionsTestResults.get(4), missionsTestResults.get(5),
                missionsTestResults.get(6)),
            "Russia", List.of(missionsTestResults.get(7), missionsTestResults.get(8), missionsTestResults.get(9))
        );

        Map<String, Collection<Mission>> result = mjtSpaceScanner.getMissionsPerCountry();

        Assertions.assertEquals(expectedResult, result, "getMissionsPerCountry(...) should return correct collection.");
    }

    @Test
    void testGetMissionsPerCountryWithEmptyDataset() {
        Map<String, Collection<Mission>> result = emptyMjtSpaceScanner.getMissionsPerCountry();

        Assertions.assertEquals(Map.of(), result, "getMissionsPerCountry(...) should return empty collection.");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWhenNIsLessOrEqualToZero() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getTopNLeastExpensiveMissions(0, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE),
            "getTopNLeastExpensiveMissions(...) should return IllegalArgumentException when called with zero for n.");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getTopNLeastExpensiveMissions(-1, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE),
            "getTopNLeastExpensiveMissions(...) should return IllegalArgumentException when called with negative number for n.");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNullMissionStatus() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getTopNLeastExpensiveMissions(1, null, RocketStatus.STATUS_ACTIVE),
            "getTopNLeastExpensiveMissions(...) should return IllegalArgumentException when called with null for missionStatus.");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNullRocketStatus() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getTopNLeastExpensiveMissions(1, MissionStatus.SUCCESS, null),
            "getTopNLeastExpensiveMissions(...) should return IllegalArgumentException when called with null for rocketStatus.");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithFilledDataset() {
        List<Mission> expectedResult =
            List.of(missionsTestResults.get(1), missionsTestResults.get(4), missionsTestResults.get(6));

        List<Mission> result =
            mjtSpaceScanner.getTopNLeastExpensiveMissions(3, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE);

        Assertions.assertIterableEquals(expectedResult, result,
            "getTopNLeastExpensiveMissions(...) should return correct List<Mission> when called with correct input.");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithEmptyDataset() {
        List<Mission> result =
            emptyMjtSpaceScanner.getTopNLeastExpensiveMissions(3, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE);

        Assertions.assertIterableEquals(List.of(), result,
            "getTopNLeastExpensiveMissions(...) should return empty collection.");
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompanyWithFilledDataset() {
        Map<String, String> expectedResult = Map.of(
            "SpaceX", "LC-39A, Kennedy Space Center, Florida, USA",
            "CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China",
            "Roscosmos", "Site 31/6, Baikonur Cosmodrome, Kazakhstan",
            "RVSN USSR", "Site 16/2, Plesetsk Cosmodrome, Russia"
        );

        Map<String, String> result = mjtSpaceScanner.getMostDesiredLocationForMissionsPerCompany();

        Assertions.assertEquals(expectedResult, result,
            "getMostDesiredLocationForMissionsPerCompany(...) should return correct collection.");
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompanyWithEmptyDataset() {
        Map<String, String> result = emptyMjtSpaceScanner.getMostDesiredLocationForMissionsPerCompany();

        Assertions.assertEquals(Map.of(), result,
            "getMostDesiredLocationForMissionsPerCompany(...) should return empty collection.");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithNullValues() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(null, LocalDate.now()),
            "getLocationWithMostSuccessfulMissionsPerCompany(...) should throw IllegalArgumentException when called with null.");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.now(), null),
            "getLocationWithMostSuccessfulMissionsPerCompany(...) should throw IllegalArgumentException when called with null.");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithInvalidTimes() {
        LocalDate startDate = LocalDate.of(2023, 12, 25);
        LocalDate endDate = LocalDate.of(2023, 12, 20);

        Assertions.assertThrows(TimeFrameMismatchException.class,
            () -> mjtSpaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(startDate, endDate),
            "getLocationWithMostSuccessfulMissionsPerCompany(...) should throw TimeFrameMismatchException when called with invalid dates.");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithCorrectDate() {
        Map<String, String> expectedResult = Map.of(
            "SpaceX", "LC-39A, Kennedy Space Center, Florida, USA",
            "CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China",
            "Roscosmos", "Site 31/6, Baikonur Cosmodrome, Kazakhstan",
            "RVSN USSR", "Site 16/2, Plesetsk Cosmodrome, Russia"
        );

        LocalDate startDate = LocalDate.of(1986, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);

        Map<String, String> result =
            mjtSpaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(startDate, endDate);

        Assertions.assertEquals(expectedResult, result,
            "getLocationWithMostSuccessfulMissionsPerCompany(...) should return correct collection.");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithCorrectDateAndEmptyDataset() {
        LocalDate startDate = LocalDate.of(1986, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);

        Map<String, String> result =
            emptyMjtSpaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(startDate, endDate);

        Assertions.assertEquals(Map.of(), result,
            "getLocationWithMostSuccessfulMissionsPerCompany(...) should return empty collection.");
    }

    @Test
    void testGetAllRocketsInFilledDataset() {
        Collection<Rocket> result = mjtSpaceScanner.getAllRockets();

        Assertions.assertIterableEquals(rocketsTestResults, result, "getAllRockets() should return correct collection.");
    }

    @Test
    void testGetAllRocketsInEmptyDataset() {
        Collection<Rocket> result = emptyMjtSpaceScanner.getAllRockets();

        Assertions.assertIterableEquals(List.of(), result, "getAllMissions() should return empty collection.");
    }

    @Test
    void testGetTopNTallestRocketsNIsLessOrEqualToZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> mjtSpaceScanner.getTopNTallestRockets(0),
            "getTopNTallestRockets() should throw IllegalArgumentException when called with negative n.");

        Assertions.assertThrows(IllegalArgumentException.class, () -> mjtSpaceScanner.getTopNTallestRockets(-1),
            "getTopNTallestRockets() should throw IllegalArgumentException when called with zero value of n.");
    }

    @Test
    void testGetTopNTallestRocketsWithCorrectDataInFilledDataset() {
        List<Rocket> expectedResult =
            List.of(rocketsTestResults.get(6), rocketsTestResults.get(9), rocketsTestResults.get(8));

        List<Rocket> result = mjtSpaceScanner.getTopNTallestRockets(3);

        Assertions.assertIterableEquals(expectedResult, result,
            "getTopNTallestRockets(...) should return correct collection.");
    }

    @Test
    void testGetTopNTallestRocketsWithCorrectDataInEmptyDataset() {
        List<Rocket> result = emptyMjtSpaceScanner.getTopNTallestRockets(3);

        Assertions.assertIterableEquals(List.of(), result,
            "getTopNTallestRockets(...) should return empty collection.");
    }

    @Test
    void testGetWikiPageForRocketInFilledDataset() {
        Map<String, Optional<String>> expectedResult = new HashMap<>();

        expectedResult.put("Tsyklon-3", Optional.of("https://en.wikipedia.org/wiki/Tsyklon-3"));
        expectedResult.put("Tsyklon-4M", Optional.of("https://en.wikipedia.org/wiki/Cyclone-4M"));
        expectedResult.put("Unha-2", Optional.of("https://en.wikipedia.org/wiki/Unha"));
        expectedResult.put("Unha-3", Optional.of("https://en.wikipedia.org/wiki/Unha"));
        expectedResult.put("Delta A", Optional.of("https://en.wikipedia.org/wiki/Delta_A"));
        expectedResult.put("Poliot", Optional.empty());
        expectedResult.put("Falcon 9 Block 5", Optional.of("https://en.wikipedia.org/wiki/Falcon_9"));
        expectedResult.put("Long March 2D", Optional.of("https://en.wikipedia.org/wiki/Long_March_2D"));
        expectedResult.put("Starship Prototype", Optional.of("https://en.wikipedia.org/wiki/SpaceX_Starship"));
        expectedResult.put("Proton-M/Briz-M", Optional.of("https://en.wikipedia.org/wiki/Proton-M"));
        expectedResult.put("Soyuz 2.1a", Optional.of("https://en.wikipedia.org/wiki/Soyuz-2"));
        expectedResult.put("Molniya-M /Block 2BL", Optional.of("https://en.wikipedia.org/wiki/Molniya-M"));

        Map<String, Optional<String>> result = mjtSpaceScanner.getWikiPageForRocket();

        Assertions.assertEquals(expectedResult, result, "getWikiPageForRocket(...) should return correct collection.");
    }

    @Test
    void testGetWikiPageForRocketInEmptyDataset() {
        Map<String, Optional<String>> result = emptyMjtSpaceScanner.getWikiPageForRocket();

        Assertions.assertEquals(Map.of(), result, "getWikiPageForRocket(...) should return empty collection.");
    }


    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWhenNIsLessOrEqualToZero() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(0, MissionStatus.SUCCESS,
                RocketStatus.STATUS_ACTIVE),
            "getWikiPagesForRocketsUsedInMostExpensiveMissions(...) should return IllegalArgumentException when called with zero for n.");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(-1, MissionStatus.SUCCESS,
                RocketStatus.STATUS_ACTIVE),
            "getWikiPagesForRocketsUsedInMostExpensiveMissions(...) should return IllegalArgumentException when called with negative number for n.");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNullMissionStatus() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(1, null,
                RocketStatus.STATUS_ACTIVE),
            "getWikiPagesForRocketsUsedInMostExpensiveMissions(...) should return IllegalArgumentException when called with null for missionStatus.");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNullRocketStatus() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(1, MissionStatus.SUCCESS, null),
            "getWikiPagesForRocketsUsedInMostExpensiveMissions(...) should return IllegalArgumentException when called with null for rocketStatus.");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithFilledDataset() {
        List<String> expectedResult = List.of(
            "https://en.wikipedia.org/wiki/Proton-M",
            "https://en.wikipedia.org/wiki/Falcon_9",
            "https://en.wikipedia.org/wiki/Soyuz-2"
        );

        List<String> result =
            mjtSpaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(3, MissionStatus.SUCCESS,
                RocketStatus.STATUS_ACTIVE);

        Assertions.assertIterableEquals(expectedResult, result,
            "getTopNLeastExpensiveMissions(...) should return correct List<String> when called with correct input.");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithEmptyDataset() {
        List<String> result =
            emptyMjtSpaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(3,
                MissionStatus.SUCCESS,
                RocketStatus.STATUS_ACTIVE);

        Assertions.assertIterableEquals(List.of(), result,
            "getWikiPagesForRocketsUsedInMostExpensiveMissions(...) should return empty collection.");
    }

    @Test
    void testSaveMostReliableRocketWithNullValues() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.saveMostReliableRocket(null, LocalDate.now(), LocalDate.now()),
            "saveMostReliableRocket(...) should throw IllegalArgumentException when called with null.");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.saveMostReliableRocket(new ByteArrayOutputStream(), null, LocalDate.now()),
            "saveMostReliableRocket(...) should throw IllegalArgumentException when called with null.");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScanner.saveMostReliableRocket(new ByteArrayOutputStream(), LocalDate.now(), null),
            "saveMostReliableRocket(...) should throw IllegalArgumentException when called with null.");
    }

    @Test
    void testSaveMostReliableRocketWithInvalidTimes() {
        LocalDate startDate = LocalDate.of(2023, 12, 25);
        LocalDate endDate = LocalDate.of(2023, 12, 20);

        Assertions.assertThrows(TimeFrameMismatchException.class,
            () -> mjtSpaceScanner.saveMostReliableRocket(new ByteArrayOutputStream(), startDate, endDate),
            "saveMostReliableRocket(...) should throw TimeFrameMismatchException when called with invalid dates.");
    }

    @Test
    void testSaveMostReliableRocketWithCorrectDate() {
        OutputStream outputStream = new ByteArrayOutputStream();
        LocalDate startDate = LocalDate.of(1986, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);

        Assertions.assertDoesNotThrow(() -> mjtSpaceScanner.saveMostReliableRocket(outputStream, startDate, endDate),
            "saveMostReliableRocket(...) should not throw exception when called with correct values.");

        Assertions.assertEquals("�oO.�bE�o�#4�xx", outputStream.toString(),    // Proton-M/Briz-M
            "Result in outputStream from saveMostReliableRocket(...) should be the correct encrypted rocket name.");
    }
}
