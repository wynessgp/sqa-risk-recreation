package domain;

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
        // Note that this should always return True
        // Since wildcards aren't assigned a territory.
        
        assertTrue(unitUnderTest.matchesTerritory(afghan));
        assertTrue(unitUnderTest.matchesTerritory(alaska));
        assertTrue(unitUnderTest.matchesTerritory(westernEurope));
        assertTrue(unitUnderTest.matchesTerritory(westernAustralia));
        assertTrue(unitUnderTest.matchesTerritory(argentina));
        assertTrue(unitUnderTest.matchesTerritory(egypt));
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
        // should always return True

        assertTrue(unitUnderTest.matchesContinent(africa));
        assertTrue(unitUnderTest.matchesContinent(northAmerica));
        assertTrue(unitUnderTest.matchesContinent(southAmerica));
        assertTrue(unitUnderTest.matchesContinent(europe));
        assertTrue(unitUnderTest.matchesContinent(oceania));
        assertTrue(unitUnderTest.matchesContinent(asia));
    }
}
