package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class TerritoryTest {

    @ParameterizedTest
    @EnumSource(PlayerColor.class)
    public void test00_setPlayerInControl_inputSetup_anyUnderlying_expectException(PlayerColor underlyingColor) {
        Territory unitUnderTest = new Territory(underlyingColor, TerritoryType.ALASKA);

        String expectedMessage = "Cannot set the player in control to setup";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.setPlayerInControl(PlayerColor.SETUP));

        String actual = exception.getMessage();
        assertEquals(expectedMessage, actual);
    }

    private static Stream<Arguments> generateAllPlayerColorsMinusSetup() {
        Set<PlayerColor> playerColors = new HashSet<>(Set.of(PlayerColor.values()));
        playerColors.remove(PlayerColor.SETUP);
        return playerColors.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("generateAllPlayerColorsMinusSetup")
    public void test01_setPlayerInControl_inputSameAsUnderlying_expectException(PlayerColor inputColor) {
        Territory unitUnderTest = new Territory(inputColor, TerritoryType.ALASKA);

        String expectedMessage = "Territory is already controlled by that player";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.setPlayerInControl(inputColor));

        String actual = exception.getMessage();
        assertEquals(expectedMessage, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void test03_setNumArmiesPresent_invalidNumber_expectException(int illegalInput) {
        Territory territory = new Territory(TerritoryType.ALASKA);

        String expectedMessage = "Number of armies to set should be greater than 0";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> territory.setNumArmiesPresent(illegalInput));

        String actual = exception.getMessage();
        assertEquals(expectedMessage, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 24})
    void test04_setNumArmiesPresent_validNumber_expectTrue(int validInput) {
        Territory territory = new Territory(TerritoryType.ALASKA);

        assertTrue(territory.setNumArmiesPresent(validInput));
        assertEquals(validInput, territory.getNumArmiesPresent());
    }

}
