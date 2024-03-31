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
    void test00_setPlayerInControl_setPlayer_expectTrue() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player(PlayerColor.BLUE);

        assertTrue(territory.setPlayerInControl(playerA));
        assertEquals(playerA, territory.getPlayerInControl());
    }

    @Test
    void test01_setPlayerInControl_twoDifferentPlayers_expectTrue() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player(PlayerColor.RED);
        Player playerB = new Player(PlayerColor.PURPLE);

        assertTrue(territory.setPlayerInControl(playerA));
        assertTrue(territory.setPlayerInControl(playerB));
        assertEquals(playerB, territory.getPlayerInControl());
    }

    @Test
    void test02_setPlayerInControl_samePlayerTwice_expectFalse() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player(PlayerColor.GREEN);

        assertTrue(territory.setPlayerInControl(playerA));
        assertFalse(territory.setPlayerInControl(playerA));
        assertEquals(playerA, territory.getPlayerInControl());
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
