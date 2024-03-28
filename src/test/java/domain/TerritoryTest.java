package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TerritoryTest {

    @Test
    void test00_SetPlayerInControl_SetPlayer_ReturnsTrue() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player();
        assertTrue(territory.setPlayerInControl(playerA));
    }

    @Test
    void test03_SetNullPlayerInControl_SetNull_ReturnsFalse() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player();
        Player playerB = null;
        territory.setPlayerInControl(playerA);
        assertFalse(territory.setPlayerInControl(playerB));
    }

    @Test
    void test04_SetPlayerInControl_SetSamePlayer_ReturnsFalse() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        assertFalse(territory.setPlayerInControl(playerA));
    }

    @Test
    void test05_SetNumArmiesPresent_SetValidNumberFive_ReturnsTrue() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertTrue(territory.setNumArmiesPresent(5));
    }

    @Test
    void test06_SetNumArmiesPresent_SetValidNumberZero_ReturnsTrue() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertTrue(territory.setNumArmiesPresent(0));
    }

    @Test
    void test07_SetNumArmiesPresent_SetValidNumberOne_ReturnsTrue() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertTrue(territory.setNumArmiesPresent(1));
    }

    @Test
    void test08_SetNumArmiesPresent_InvalidNegativeNumber_ReturnsFalse() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertFalse(territory.setNumArmiesPresent(-1));
    }

    @Test
    void test09_GetPlayerInControl_DefaultPlayer_ReturnsNull() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertNull(territory.getPlayerInControl());
    }

    @Test
    void test10_GetPlayerInControl_SetPlayer_ReturnsPlayer() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        assertSame(playerA, territory.getPlayerInControl());
    }

    @Test
    void test11_GetPlayerInControl_ChangePlayer_ConfirmAndReturnsChangedPlayer() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player();
        Player playerB = new Player();
        territory.setPlayerInControl(playerA);
        assertTrue(territory.setPlayerInControl(playerB));
        assertSame(playerB, territory.getPlayerInControl());
    }

    @Test
    void test12_GetNumArmiesPresent_ReturnsZero() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertEquals(0, territory.getNumArmiesPresent());
    }

    @Test
    void test13_GetNumArmiesPresent_SetValidNumberFive_ReturnsNumber() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        int validNumberOfArmies = 5;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test14_GetTerritoryType_DefaultType_ReturnsNull() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertNull(territory.getTerritoryType());
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
