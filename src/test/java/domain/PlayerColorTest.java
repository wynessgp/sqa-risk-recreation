package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class PlayerColorTest {

    private static Stream<Arguments> playerColorGenerator() {
        return Stream.of(
            Arguments.of(PlayerColor.SETUP, "Setup"),
            Arguments.of(PlayerColor.BLACK, "Black"),
            Arguments.of(PlayerColor.RED, "Red"),
            Arguments.of(PlayerColor.YELLOW, "Yellow"),
            Arguments.of(PlayerColor.BLUE, "Blue"),
            Arguments.of(PlayerColor.GREEN, "Green"),
            Arguments.of(PlayerColor.PURPLE, "Purple")
        );
    }

    @ParameterizedTest
    @MethodSource("playerColorGenerator")
    public void test00_toString_withValidColor_returnsProperString(PlayerColor color, String expected) {
        String result = color.toString();
        assertEquals(expected, result);
    }
}
