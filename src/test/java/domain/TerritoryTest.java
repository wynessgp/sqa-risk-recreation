package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void test00_SetPlayerInControl_ReturnsTrue() {
        Territory territory = new Territory(null);
        Player playerA = new Player();
        assertTrue(territory.setPlayerInControl(playerA));
    }


    @Test
    void test02_ChangePlayerInControl() {
        Territory territory = new Territory(null);
        Player playerA = new Player();
        Player playerB = new Player();
        territory.setPlayerInControl(playerA);
        assertTrue(territory.setPlayerInControl(playerB));
    }

    @Test
    void test12_SetPlayerInControl_ReturnsFalse() {
        Territory territory = new Territory(null);
        Player playerA = new Player();
        Player playerB = null;
        territory.setPlayerInControl(playerA);
        assertFalse(territory.setPlayerInControl(playerB));
    }

    @Test
    void test14_SetPlayerInControl_ReturnsFalse() {
        Territory territory = new Territory(null);
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        assertFalse(territory.setPlayerInControl(playerA));
    }

    @Test
    void test03_SetNumArmiesPresent_ReturnsTrueForValidNumber() {
        Territory territory = new Territory(null);
        assertTrue(territory.setNumArmiesPresent(5));
    }


    @Test
    void test05_SetNumArmiesPresent_ReturnsTrueForZero() {
        Territory territory = new Territory(null);
        assertTrue(territory.setNumArmiesPresent(0));
    }


    @Test
    void test07_SetNumArmiesPresent_ReturnsTrueForOne() {
        Territory territory = new Territory(null);
        assertTrue(territory.setNumArmiesPresent(1));
    }


    @Test
    void test09_SetNumArmiesPresent_ReturnsFalseForInvalidNumber() {
        Territory territory = new Territory(null);
        assertFalse(territory.setNumArmiesPresent(-1));
    }

    @Test
    void test10_GetPlayerInControl_ReturnsNull() {
        Territory territory = new Territory(null);
        assertNull(territory.getPlayerInControl());
    }

    @Test
    void test14_GetPlayerInControl_ReturnsPlayer() {
        Territory territory = new Territory(null);
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        assertSame(playerA, territory.getPlayerInControl());
    }

    @Test
    void test15_GetNumArmiesPresent_ReturnsZero() {
        Territory territory = new Territory(null);
        assertEquals(0, territory.getNumArmiesPresent());
    }

    @Test
    void test16_GetNumArmiesPresent_ReturnsNumber() {
        Territory territory = new Territory(null);
        int validNumberOfArmies = 5;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test17_GetTerritoryType_ReturnsTerritoryType() {
        Territory territory = new Territory(null);
        assertNull(territory.getTerritoryType());
    }

    @Test
    void test18_GetTerritoryType_ReturnsTerritoryType() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        TerritoryType territoryType = TerritoryType.ALASKA;
        assertEquals(territoryType, territory.getTerritoryType());
    }

    @Test
    void test19_GetTerritoryType_ReturnsTerritoryType() {
        Territory territory = new Territory(TerritoryType.WESTERN_AUSTRALIA);
        TerritoryType territoryType = TerritoryType.WESTERN_AUSTRALIA;
        assertSame(territoryType, territory.getTerritoryType());
    }

}
