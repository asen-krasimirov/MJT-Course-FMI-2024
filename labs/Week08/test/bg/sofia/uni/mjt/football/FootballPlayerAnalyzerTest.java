package bg.sofia.uni.mjt.football;

import bg.sofia.uni.fmi.mjt.football.FootballPlayerAnalyzer;
import bg.sofia.uni.fmi.mjt.football.Player;

import bg.sofia.uni.fmi.mjt.football.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.NoSuchElementException;

public class FootballPlayerAnalyzerTest {

    private FootballPlayerAnalyzer footballPlayerAnalyzer;
    private static ArrayList<Player> testPlayers;

    @BeforeAll
    static void setUpTestCase() {
        Player player1 = Player.of(
            "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left"
        );

        Player player2 = Player.of(
            "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right"
        );

        Player player3 = Player.of(
            "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right"
        );

        Player player4 = Player.of(
            "L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right"
        );

        Player player5 = Player.of(
            "K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right"
        );

        Player player6 = Player.of(
            "S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right"
        );

        testPlayers = new ArrayList<>(List.of(player1, player2, player3, player4, player5, player6));
    }

    @BeforeEach
    void setUp() {
        StringReader stringReader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot" +
                System.lineSeparator() +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left" +
                System.lineSeparator() +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right" +
                System.lineSeparator() +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right" +
                System.lineSeparator() +
                "L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right" +
                System.lineSeparator() +
                "K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right" +
                System.lineSeparator() +
                "S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right"
        );

        footballPlayerAnalyzer = new FootballPlayerAnalyzer(stringReader);
    }

    @Test
    void testGetAllPlayersWithPlayersInDataset() {
        List<Player> expectedList = List.of(
            testPlayers.get(0),
            testPlayers.get(1),
            testPlayers.get(2),
            testPlayers.get(3),
            testPlayers.get(4),
            testPlayers.get(5)
        );

        List<Player> result = footballPlayerAnalyzer.getAllPlayers();

        Assertions.assertIterableEquals(expectedList, result,
            "getAllPlayers(...) should return correct list.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add(testPlayers.get(0)),
            "getAllPlayers(...) should return unmodifiable list.");

    }

    @Test
    void testGetAllPlayersWithPlayersInEmptyDataset() {
        StringReader stringReader = new StringReader("");
        FootballPlayerAnalyzer footballPlayerAnalyzer1 = new FootballPlayerAnalyzer(stringReader);

        List<Player> expectedList = List.of();

        List<Player> result = footballPlayerAnalyzer1.getAllPlayers();

        Assertions.assertIterableEquals(expectedList, result,
            "getAllPlayers(...) should return correct list.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add(testPlayers.get(0)),
            "getAllPlayers(...) should return unmodifiable list.");
    }

    @Test
    void testGetAllNationalitiesWithPlayersInDataset() {
        Set<String> expectedSet = Set.of("Argentina", "Denmark", "France", "Italy", "Senegal");

        Set<String> result = footballPlayerAnalyzer.getAllNationalities();

        Assertions.assertEquals(expectedSet, result,
            "getAllNationalities(...) should return correct set.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add("Germany"),
            "getAllNationalities(...) should return unmodifiable set.");
    }

    @Test
    void testGetAllNationalitiesWithPlayersInEmptyDataset() {
        StringReader stringReader = new StringReader("");
        FootballPlayerAnalyzer footballPlayerAnalyzer1 = new FootballPlayerAnalyzer(stringReader);

        Set<String> expectedSet = Set.of();

        Set<String> result = footballPlayerAnalyzer1.getAllNationalities();

        Assertions.assertIterableEquals(expectedSet, result,
            "getAllNationalities(...) should return correct set.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add("Germany"),
            "getAllNationalities(...) should return unmodifiable set.");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityWithNullNationality() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> footballPlayerAnalyzer.getHighestPaidPlayerByNationality(null),
            "getHighestPaidPlayerByNationality(...) should throw IllegalArgumentException when called with null value.");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityWithNonExistingNationality() {
        Assertions.assertThrows(NoSuchElementException.class,
            () -> footballPlayerAnalyzer.getHighestPaidPlayerByNationality("Greece"),
            "getHighestPaidPlayerByNationality(...) should throw NoSuchElementException when called with non-existing nationality.");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityWithCorrectNationality() {
        Player expectedResult = testPlayers.get(0);

        Player result = footballPlayerAnalyzer.getHighestPaidPlayerByNationality("Argentina");

        Assertions.assertEquals(expectedResult, result,
            "getHighestPaidPlayerByNationality(...) should return correct player.");
    }

    @Test
    void groupByPositionReturnsCorrectData() {
        Map<Position, Set<Player>> expectedResult = Map.of(
            Position.CF, Set.of(testPlayers.get(0)),
            Position.RW, Set.of(testPlayers.get(0)),
            Position.ST, Set.of(testPlayers.get(0), testPlayers.get(3), testPlayers.get(5)),
            Position.CAM, Set.of(testPlayers.get(1), testPlayers.get(2)),
            Position.RM, Set.of(testPlayers.get(1)),
            Position.CM, Set.of(testPlayers.get(1), testPlayers.get(2)),
            Position.LW, Set.of(testPlayers.get(3)),
            Position.CB, Set.of(testPlayers.get(4))
        );

        Map<Position, Set<Player>> result = footballPlayerAnalyzer.groupByPosition();

        Assertions.assertEquals(expectedResult, result, "groupByPosition(...) should return correct result.");
    }

    @Test
    void getPlayersByFullNameKeywordNullData() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> footballPlayerAnalyzer.getPlayersByFullNameKeyword(null),
            "getPlayersByFullNameKeyword(...) should throw IllegalArgumentException when called with null value.");
    }

    @Test
    void getPlayersByFullNameKeywordCorrectData() {
        Set<Player> expectedSet =
            Set.of(testPlayers.get(0), testPlayers.get(1), testPlayers.get(3), testPlayers.get(5)
            );

        Set<Player> result = footballPlayerAnalyzer.getPlayersByFullNameKeyword("ne");

        Assertions.assertEquals(expectedSet, result, "getPlayersByFullNameKeyword(...) should return correct result.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add(testPlayers.get(2)),
            "getPlayersByFullNameKeyword(...) should return unmodifiable set.");
    }

    @Test
    void getPlayersByFullNameKeywordInvalidData() {
        Set<Player> expectedSet = Set.of();

        Set<Player> result = footballPlayerAnalyzer.getPlayersByFullNameKeyword("NE");

        Assertions.assertEquals(expectedSet, result, "getPlayersByFullNameKeyword(...) should return correct result.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add(testPlayers.get(2)),
            "getPlayersByFullNameKeyword(...) should return unmodifiable set.");
    }

}
