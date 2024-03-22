package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class WildCardTest {

    private static Stream<Arguments> territoryGenerator() {
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        return territories.stream().map(Arguments::of);
    }

    private static Stream<Arguments> pieceGenerator() {
        Set<PieceType> pieces = Set.of(PieceType.values());
        return pieces.stream().map(Arguments::of);
    }
    
    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test00_matchesTerritory_allDistinctTerritories(TerritoryType territory) {
        // make the unit under test
        Card unitUnderTest = new WildCard();
        
        // operation being tested: matchesTerritory on WildCard
        // Note that this should always return False
        // Since wildcards aren't assigned a territory, and
        // we want to prevent users from getting more units from these.
        
        assertFalse(unitUnderTest.matchesTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("pieceGenerator")
    public void test01_matchesPieceType_allDifferentPieceTypes(PieceType piece) {
        // make the unit under test
        Card unitUnderTest = new WildCard();

        // operation being tested: matchesPieceType on WildCard
        // should always return True

        assertTrue(unitUnderTest.matchesPieceType(piece));
    }

    @Test
    public void test03_isWild_singleWildCard() {
        // make the unit under test
        Card unitUnderTest = new WildCard();

        // no enum variable setup, just into the operation
        // operation being tested: isWild
        // should always return true for wild cards

        assertTrue(unitUnderTest.isWild());
    }
}
