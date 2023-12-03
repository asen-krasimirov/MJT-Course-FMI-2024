package bg.sofia.uni.mjt.football;

import bg.sofia.uni.fmi.mjt.football.FootballPlayerAnalyzer;
import bg.sofia.uni.fmi.mjt.football.Player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

public class FootballPlayerAnalyzerTest {

    private FootballPlayerAnalyzer footballPlayerAnalyzer;

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
                "K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right"
        );

        footballPlayerAnalyzer = new FootballPlayerAnalyzer(stringReader);
    }

    @Test
    void testGetAllPlayersWithPlayersInDataset() {
        List<Player> expectedList = List.of(
            Player.of(
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left"),
            Player.of(
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right"),
            Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right"),
            Player.of("L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right"),
            Player.of("K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right")
        );

        List<Player> result = footballPlayerAnalyzer.getAllPlayers();

        Assertions.assertIterableEquals(expectedList, result,
            "getAllPlayers(...) should return correct list.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add(Player.of(
                "V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right"
            )),
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
            () -> result.add(Player.of(
                "V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right"
            )),
            "getAllPlayers(...) should return unmodifiable list.");
    }

    @Test
    void testGetAllNationalitiesWithPlayersInDataset() {
        Set<String> expectedList = Set.of("Argentina", "Denmark", "France", "Italy", "Senegal");

        Set<String> result = footballPlayerAnalyzer.getAllNationalities();

        Assertions.assertEquals(expectedList, result,
            "getAllNationalities(...) should return correct set.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add("Germany"),
            "getAllNationalities(...) should return unmodifiable set.");
    }

    @Test
    void testGetAllNationalitiesWithPlayersInEmptyDataset() {
        StringReader stringReader = new StringReader("");
        FootballPlayerAnalyzer footballPlayerAnalyzer1 = new FootballPlayerAnalyzer(stringReader);

        Set<String> expectedList = Set.of();

        Set<String> result = footballPlayerAnalyzer1.getAllNationalities();

        Assertions.assertIterableEquals(expectedList, result,
            "getAllNationalities(...) should return correct set.");

        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> result.add("Germany"),
            "getAllNationalities(...) should return unmodifiable set.");
    }
}
