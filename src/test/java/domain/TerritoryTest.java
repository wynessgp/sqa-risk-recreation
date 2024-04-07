package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TerritoryTest {

    @Test
    public void test00_setPlayerInControl_inputSetup_underlyingNotSetup_expectException() {
        Territory unitUnderTest = new Territory(PlayerColor.BLUE, TerritoryType.ALASKA);

        String expectedMessage = "Cannot set the player in control to setup";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> unitUnderTest.setPlayerInControl(PlayerColor.SETUP));

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
