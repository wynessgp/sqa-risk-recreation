package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WildCardTest {
    
    @Test
    public void Test00_matchesTerritory_territoryFromAllContinents() {
        // make the unit under test
        Card unitUnderTest = new WildCard();

        // handle enum variable setup
        TerritoryType afghan = TerritoryType.AFGHANISTAN;
        TerritoryType alaska = TerritoryType.ALASKA;
        TerritoryType westernEurope = TerritoryType.WESTERN_EUROPE;
        TerritoryType westernAustralia = TerritoryType.WESTERN_AUSTRALIA;
        TerritoryType argentina = TerritoryType.ARGENTINA;
        TerritoryType egypt = TerritoryType.EGYPT;

        // operation being tested: matchesTerritory on WildCard
        // Note that this should always return False
        // Since wildcards aren't assigned a territory, and
        // we want to prevent users from getting more units from these.
        
        assertFalse(unitUnderTest.matchesTerritory(afghan));
        assertFalse(unitUnderTest.matchesTerritory(alaska));
        assertFalse(unitUnderTest.matchesTerritory(westernEurope));
        assertFalse(unitUnderTest.matchesTerritory(westernAustralia));
        assertFalse(unitUnderTest.matchesTerritory(argentina));
        assertFalse(unitUnderTest.matchesTerritory(egypt));
    }

    @Test
    public void Test01_matchesPieceType_allDifferentPieceTypes() {
        // make the unit under test
        Card unitUnderTest = new WildCard();

        // handle enum variable setup
        PieceType infantry = PieceType.INFANTRY;
        PieceType cavalry = PieceType.CAVALRY;
        PieceType artillery = PieceType.ARTILLERY;

        // operation being tested: matchesPieceType on WildCard
        // should always return True

        assertTrue(unitUnderTest.matchesPieceType(infantry));
        assertTrue(unitUnderTest.matchesPieceType(cavalry));
        assertTrue(unitUnderTest.matchesPieceType(artillery));
    }

    @Test
    public void Test02_matchesContinent_allUniqueContinents() {
        // make the unit under test
        Card unitUnderTest = new WildCard();

        // handle enum variable setup
        Continent africa = Continent.AFRICA;
        Continent northAmerica = Continent.NORTH_AMERICA;
        Continent southAmerica = Continent.SOUTH_AMERICA;
        Continent europe = Continent.EUROPE;
        Continent oceania = Continent.OCEANIA;
        Continent asia = Continent.ASIA;

        // operation being tested: matchesContinent on WildCard
        // should always return False, same reason as matchesTerritory

        assertFalse(unitUnderTest.matchesContinent(africa));
        assertFalse(unitUnderTest.matchesContinent(northAmerica));
        assertFalse(unitUnderTest.matchesContinent(southAmerica));
        assertFalse(unitUnderTest.matchesContinent(europe));
        assertFalse(unitUnderTest.matchesContinent(oceania));
        assertFalse(unitUnderTest.matchesContinent(asia));
    }

    @Test
    public void Test03_isWild_singleWildCard() {
        // make the unit under test
        Card unitUnderTest = new WildCard();

        // no enum variable setup, just into the operation
        // operation being tested: isWild
        // should always return true for wild cards

        assertTrue(unitUnderTest.isWild());
    }
}
