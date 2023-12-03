package bg.sofia.uni.mjt.football;

import bg.sofia.uni.fmi.mjt.football.Foot;
import bg.sofia.uni.fmi.mjt.football.Player;
import bg.sofia.uni.fmi.mjt.football.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class PlayerTest {

    @Test
    void testFactoryMethodOf() {
        String testLine = "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left";

        Player player = Player.of(testLine);

        Assertions.assertEquals("L. Messi", player.name(), "Player's name not parsed correctly.");
        Assertions.assertEquals("Lionel Andrés Messi Cuccittini", player.fullName(), "Player's fullName not parsed correctly.");
        Assertions.assertEquals(LocalDate.of(1987, 6, 24), player.birthDate(), "Player's birthDate not parsed correctly.");
        Assertions.assertEquals(31, player.age(), "Player's age not parsed correctly.");
        Assertions.assertEquals(170.18, player.heightCm(), "Player's heightCm not parsed correctly.");
        Assertions.assertEquals(72.1, player.weightKg(), "Player's weightKg not parsed correctly.");
        Assertions.assertIterableEquals(List.of(Position.CF, Position.RW, Position.ST), player.positions(), "Player's positions not parsed correctly.");
        Assertions.assertEquals("Argentina", player.nationality(), "Player's nationality not parsed correctly.");
        Assertions.assertEquals(94, player.overallRating(), "Player's overallRating not parsed correctly.");
        Assertions.assertEquals(94, player.potential(), "Player's potential not parsed correctly.");
        Assertions.assertEquals(110500000, player.valueEuro(), "Player's valueEuro not parsed correctly.");
        Assertions.assertEquals(565000, player.wageEuro(), "Player's wageEuro not parsed correctly.");
        Assertions.assertEquals(Foot.LEFT, player.preferredFoot(), "Player's preferredFoot not parsed correctly.");
    }

}
