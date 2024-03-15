package domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WildCardTest {
    
    @Test
    public void Test00_matchesTerritory_territoryFromAllContinents() {
        // necessary setup
        Card unitUnderTest = new WildCard();

        // handle enum variable setup
        TerritoryType afghan = TerritoryType.AFGHANISTAN;
        TerritoryType alaska = TerritoryType.ALASKA;
        TerritoryType westernEurope = TerritoryType.WESTERN_EUROPE;
        TerritoryType westernAustralia = TerritoryType.WESTERN_AUSTRALIA;
        TerritoryType argentina = TerritoryType.ARGENTINA;
        TerritoryType egypt = TerritoryType.EGYPT;

        // operation being tested: matchesTerritory on WildCard
        // Note that this should always return True
        // Since wildcards aren't assigned a territory.
        
        assertTrue(unitUnderTest.matchesTerritory(afghan));
        assertTrue(unitUnderTest.matchesTerritory(alaska));
        assertTrue(unitUnderTest.matchesTerritory(westernEurope));
        assertTrue(unitUnderTest.matchesTerritory(westernAustralia));
        assertTrue(unitUnderTest.matchesTerritory(argentina));
        assertTrue(unitUnderTest.matchesTerritory(egypt));
    }
}
