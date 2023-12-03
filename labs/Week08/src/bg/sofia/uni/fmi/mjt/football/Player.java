package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm, double weightKg,
                     List<Position> positions, String nationality, int overallRating, int potential, long valueEuro,
                     long wageEuro, Foot preferredFoot) {

    public static Player of(String line) {
        String[] tokens = line.split("\\Q;\\E");

        String name = tokens[0];
        String fullName = tokens[1];

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(tokens[2], dateFormatter);

        int age = Integer.parseInt(tokens[3]);
        double heightCm = Double.parseDouble(tokens[4]);
        double weightKg = Double.parseDouble(tokens[5]);

        List<Position> positions = new ArrayList<>();
        for (String positionStr : tokens[6].split("\\Q,\\E")) {
            positions.add(Position.valueOf(positionStr));
        }

        String nationality = tokens[7];
        int overallRating = Integer.parseInt(tokens[8]);
        int potential = Integer.parseInt(tokens[9]);
        long valueEuro = Long.parseLong(tokens[10]);
        long wageEuro = Long.parseLong(tokens[11]);

        Foot preferredFoot = Foot.valueOf(tokens[12]);

        return new Player(name, fullName, birthDate, age, heightCm, weightKg, positions, nationality, overallRating,
            potential, valueEuro, wageEuro, preferredFoot);
    }

}
