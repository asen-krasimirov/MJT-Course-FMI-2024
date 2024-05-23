package bg.sofia.uni.fmi.mjt.spotify.command;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CommandCreatorTest {
    @Test
    void testNewCommandWithValidCommand() {
        String input = "test-command arg1 arg2";

        Command result = CommandCreator.newCommand(input);

        Command expectedResult = new Command("test-command", new String[] {"arg1", "arg2"});

        assertEquals(expectedResult.command(), result.command(), "newCommand(...) should create correct command.");

        assertArrayEquals(expectedResult.arguments(), result.arguments(), "newCommand(...) should create correct command.");
    }
}
