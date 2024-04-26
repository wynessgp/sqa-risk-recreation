package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BattleResultTest {

    private static Stream<Arguments> battleResultGenerator() {
        return Stream.of(
                Arguments.of(BattleResult.DEFENDER_VICTORY, "Defender victory"),
                Arguments.of(BattleResult.ATTACKER_VICTORY, "Attacker victory")
        );
    }

    @ParameterizedTest
    @MethodSource("battleResultGenerator")
    public void test00_toString_withValidResult_returnsProperString(BattleResult battleResult, String expected) {
        String result = battleResult.toString();
        assertEquals(expected, result);
    }
}
