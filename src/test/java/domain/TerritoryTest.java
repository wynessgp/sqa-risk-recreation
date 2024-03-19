package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void test00_SetPlayerInControl_ReturnsTrue() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player();
        assertTrue(territory.setPlayerInControl(playerA));
    }


    @Test
    void test01_SetPlayerInControl_UpdatesPlayerSuccessfully() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player();
        territory.setPlayerInControl(playerA);
        assertSame(playerA, territory.getPlayerInControl());
    }

    @Test
    void test02_ChangePlayerInControl() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        Player playerA = new Player();
        Player playerB = new Player();
        territory.setPlayerInControl(playerA);
        assertTrue(territory.setPlayerInControl(playerB));
    }

    @Test
    void test03_SetNumArmiesPresent_ReturnsTrueForValidNumber() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertTrue(territory.setNumArmiesPresent(5));
    }

    @Test
    void test04_SetNumArmiesPresent_ActuallySetsTheNumber() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        int validNumberOfArmies = 5;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test05_SetNumArmiesPresent_ReturnsTrueForZero() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertTrue(territory.setNumArmiesPresent(0));
    }

    @Test
    void test06_SetNumArmiesPresent_ActuallySetsZero() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        int validNumberOfArmies = 0;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test07_SetNumArmiesPresent_ReturnsTrueForOne() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertTrue(territory.setNumArmiesPresent(1));
    }

    @Test
    void test08_SetNumArmiesPresent_ActuallySetsOne() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        int validNumberOfArmies = 1;
        territory.setNumArmiesPresent(validNumberOfArmies);
        assertEquals(validNumberOfArmies, territory.getNumArmiesPresent());
    }

    @Test
    void test09_SetNumArmiesPresent_ReturnsFalseForInvalidNumber() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertFalse(territory.setNumArmiesPresent(-1));
    }

    @Test
    void test10_TerritoryInitialization() {
        Territory territory = new Territory(TerritoryType.ALASKA);
        assertEquals(TerritoryType.ALASKA, territory.getType());
    }




}
