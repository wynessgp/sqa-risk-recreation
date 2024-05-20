package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GamePhaseTest {

    private static Stream<Arguments> gamePhaseGenerator() {
        return Stream.of(
                Arguments.of(GamePhase.SETUP, "Setup"),
                Arguments.of(GamePhase.PLACEMENT, "Placement"),
                Arguments.of(GamePhase.SCRAMBLE, "Scramble"),
                Arguments.of(GamePhase.ATTACK, "Attack"),
                Arguments.of(GamePhase.FORTIFY, "Fortify"),
                Arguments.of(GamePhase.GAME_OVER, "Game over")
        );
    }

    @ParameterizedTest
    @MethodSource("gamePhaseGenerator")
    public void test00_toString_withValidResult_returnsProperString(GamePhase phase, String expected) {
        String result = phase.toString();
        assertEquals(expected, result);
    }

}
