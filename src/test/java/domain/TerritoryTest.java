package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {


    @Test
    void testSetPlayerInControl() {
        Territory territory = new Territory();
        Player mockPlayerA = EasyMock.mock(Player.class);
        Player mockPlayerB = EasyMock.mock(Player.class);
        replay(mockPlayerA, mockPlayerB);
        assertTrue(territory.setPlayerInControl(mockPlayerA));
        assertTrue(territory.setPlayerInControl(mockPlayerB));
        verify(mockPlayerA, mockPlayerB);
    }

    @Test
    void testSetNumArmiesPresent() {
        Territory territory = new Territory();
        assertTrue(territory.setNumArmiesPresent(5));
        assertTrue(territory.setNumArmiesPresent(0));
        assertTrue(territory.setNumArmiesPresent(1));
        assertFalse(territory.setNumArmiesPresent(-1));
    }
    //split the tests
}
