package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
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
    }

    @Test
    void test01_setPlayerInControl_twoDifferentPlayers_expectTrue() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player(PlayerColor.RED);
        Player playerB = new Player(PlayerColor.PURPLE);

        assertTrue(territory.setPlayerInControl(playerA));
        assertTrue(territory.setPlayerInControl(playerB));
    }

    @Test
    void test02_setPlayerInControl_samePlayerTwice_expectFalse() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player(PlayerColor.GREEN);

        assertTrue(territory.setPlayerInControl(playerA));
        assertFalse(territory.setPlayerInControl(playerA));
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

    @Test
    void test10_GetPlayerInControl_SetPlayer_ReturnsPlayer() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player(PlayerColor.GREEN);
        territory.setPlayerInControl(playerA);
        assertSame(playerA, territory.getPlayerInControl());
    }

    @Test
    void test11_GetPlayerInControl_ChangePlayer_ConfirmAndReturnsChangedPlayer() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player(PlayerColor.PURPLE);
        Player playerB = new Player(PlayerColor.YELLOW);
        territory.setPlayerInControl(playerA);
        assertTrue(territory.setPlayerInControl(playerB));
        assertSame(playerB, territory.getPlayerInControl());
    }

    @Test
    void test13_GetNumArmiesPresent_SetValidNumberFive_ReturnsNumber() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        int validNumberOfArmies = 5;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test15_GetTerritoryType_SetValidTypeAlaska_ReturnsTerritoryType() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        TerritoryType territoryType = TerritoryType.ALASKA;
        assertEquals(territoryType, territory.getTerritoryType());
    }

    @Test
    void test16_GetTerritoryType_SetValidTypeWesternAustralia_ReturnsTerritoryType() {
        Territory territory = new Territory(TerritoryType.WESTERN_AUSTRALIA);
        TerritoryType territoryType = TerritoryType.WESTERN_AUSTRALIA;
        assertSame(territoryType, territory.getTerritoryType());
    }

}
