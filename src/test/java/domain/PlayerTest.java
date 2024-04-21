package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void test00_ownsTerritory_withNoneOwned_returnsFalse() {
        Player player = new Player();
        TerritoryType territory = TerritoryType.ALASKA;
        assertFalse(player.ownsTerritory(territory));
    }

}
