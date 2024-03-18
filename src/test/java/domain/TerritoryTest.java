package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void testSetPlayerInControlSuccessfully() {
        Territory territory = new Territory();
        Player playerA = new Player();
        assertTrue(territory.setPlayerInControl(playerA));
    }

    @Test
    void testChangePlayerInControl() {
        Territory territory = new Territory();
        Player playerA = new Player();
        Player playerB = new Player();
        territory.setPlayerInControl(playerA);
        assertTrue(territory.setPlayerInControl(playerB));
    }

    @Test
    void testSetNumArmiesPresentWithValidNumber() {
        Territory territory = new Territory();
        assertTrue(territory.setNumArmiesPresent(5));
    }

    @Test
    void testSetNumArmiesPresentWithZero() {
        Territory territory = new Territory();
        assertTrue(territory.setNumArmiesPresent(0));
    }

    @Test
    void testSetNumArmiesPresentWithOne() {
        Territory territory = new Territory();
        assertTrue(territory.setNumArmiesPresent(1));
    }

    @Test
    void testSetNumArmiesPresentWithInvalidNumber() {
        Territory territory = new Territory();
        assertFalse(territory.setNumArmiesPresent(-1));
    }
}
