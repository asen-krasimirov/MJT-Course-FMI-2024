package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_ZERO;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_ONE;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_TWO;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_THREE;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_FOUR;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_FIVE;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_SIX;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_SEVEN;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_EIGHT;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_NINE;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_TEN;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_ELEVEN;
import static bg.sofia.uni.fmi.mjt.football.TokenIndex.INDEX_TWELVE;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm, double weightKg,
                     List<Position> positions, String nationality, int overallRating, int potential, long valueEuro,
                     long wageEuro, Foot preferredFoot) {

    public static Player of(String line) {
        String[] tokens = line.split("\\Q;\\E");

        String name = tokens[INDEX_ZERO.getIndex()];
        String fullName = tokens[INDEX_ONE.getIndex()];

        String[] birthDateTokens = tokens[INDEX_TWO.getIndex()].split("\\Q/\\E");
        LocalDate birthDate = LocalDate.of(Integer.parseInt(birthDateTokens[INDEX_TWO.getIndex()]),
            Integer.parseInt(birthDateTokens[INDEX_ZERO.getIndex()]),
            Integer.parseInt(birthDateTokens[INDEX_ONE.getIndex()]));

        int age = Integer.parseInt(tokens[INDEX_THREE.getIndex()]);
        double heightCm = Double.parseDouble(tokens[INDEX_FOUR.getIndex()]);
        double weightKg = Double.parseDouble(tokens[INDEX_FIVE.getIndex()]);

        List<Position> positions = new ArrayList<>();
        for (String positionStr : tokens[INDEX_SIX.getIndex()].split("\\Q,\\E")) {
            positions.add(Position.valueOf(positionStr));
        }

        String nationality = tokens[INDEX_SEVEN.getIndex()];
        int overallRating = Integer.parseInt(tokens[INDEX_EIGHT.getIndex()]);
        int potential = Integer.parseInt(tokens[INDEX_NINE.getIndex()]);
        long valueEuro = Long.parseLong(tokens[INDEX_TEN.getIndex()]);
        long wageEuro = Long.parseLong(tokens[INDEX_ELEVEN.getIndex()]);
        Foot preferredFoot = Foot.valueOf(tokens[INDEX_TWELVE.getIndex()].toUpperCase());

        return new Player(name, fullName, birthDate, age, heightCm, weightKg, positions, nationality, overallRating,
            potential, valueEuro, wageEuro, preferredFoot);
    }

}
