package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void test00_SetPlayerInControl_ReturnsTrue() {
        Territory territory = new Territory();
        Player playerA = new Player();
        assertTrue(territory.setPlayerInControl(playerA));
    }


    @Test
    void test01_SetPlayerInControl_UpdatesPlayerSuccessfully() {
        Territory territory = new Territory();
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        assertSame(playerA, territory.getPlayerInControl());
    }

    @Test
    void test02_ChangePlayerInControl() {
        Territory territory = new Territory();
        Player playerA = new Player();
        Player playerB = new Player();
        territory.setPlayerInControl(playerA);
        assertTrue(territory.setPlayerInControl(playerB));
    }

    @Test
    void test11_ChangePlayerInControlSuccessfully() {
        Territory territory = new Territory();
        Player playerA = new Player();
        Player playerB = new Player();
        territory.setPlayerInControl(playerA);
        territory.setPlayerInControl(playerB);
        assertSame(playerB, territory.getPlayerInControl());
    }


    @Test
    void test12_SetPlayerInControl_ReturnsFalse() {
        Territory territory = new Territory();
        Player playerA = new Player();
        Player playerB = null;
        territory.setPlayerInControl(playerA);
        assertFalse(territory.setPlayerInControl(playerB));
    }

    @Test
    void test13_ChangeSamePlayerInControlSuccessfully() {
        Territory territory = new Territory();
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        territory.setPlayerInControl(playerA);
        assertSame(playerA, territory.getPlayerInControl());
    }

    @Test
    void test14_SetPlayerInControl_ReturnsFalse() {
        Territory territory = new Territory();
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        assertFalse(territory.setPlayerInControl(playerA));
    }

    @Test
    void test03_SetNumArmiesPresent_ReturnsTrueForValidNumber() {
        Territory territory = new Territory();
        assertTrue(territory.setNumArmiesPresent(5));
    }

    @Test
    void test04_SetNumArmiesPresent_ActuallySetsTheNumber() {
        Territory territory = new Territory();
        int validNumberOfArmies = 5;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test05_SetNumArmiesPresent_ReturnsTrueForZero() {
        Territory territory = new Territory();
        assertTrue(territory.setNumArmiesPresent(0));
    }

    @Test
    void test06_SetNumArmiesPresent_ActuallySetsZero() {
        Territory territory = new Territory();
        int validNumberOfArmies = 0;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test07_SetNumArmiesPresent_ReturnsTrueForOne() {
        Territory territory = new Territory();
        assertTrue(territory.setNumArmiesPresent(1));
    }

    @Test
    void test08_SetNumArmiesPresent_ActuallySetsOne() {
        Territory territory = new Territory();
        int validNumberOfArmies = 1;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test09_SetNumArmiesPresent_ReturnsFalseForInvalidNumber() {
        Territory territory = new Territory();
        assertFalse(territory.setNumArmiesPresent(-1));
    }

    @Test
    void test10_GetPlayerInControl_ReturnsNull() {
        Territory territory = new Territory();
        assertNull(territory.getPlayerInControl());
    }

    @Test
    void test14_GetPlayerInControl_ReturnsPlayer() {
        Territory territory = new Territory();
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        assertSame(playerA, territory.getPlayerInControl());
    }

    @Test
    void test15_GetNumArmiesPresent_ReturnsZero() {
        Territory territory = new Territory();
        assertEquals(0, territory.getNumArmiesPresent());
    }

    @Test
    void test16_GetNumArmiesPresent_ReturnsNumber() {
        Territory territory = new Territory();
        int validNumberOfArmies = 5;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

}
