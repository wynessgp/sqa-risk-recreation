package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TerritoryCardTest {
    private static Stream<Arguments> territoryGenerator() {
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        return territories.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test00_matchesTerritory_checkAllTrueInputs(TerritoryType territory) {
        TerritoryCard card = new TerritoryCard(territory, ArmyType.INFANTRY);
        assertTrue(card.matchesTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test01_matchesTerritory_checkAllFalseInputs(TerritoryType territoryOnCard) {
        TerritoryCard card = new TerritoryCard(territoryOnCard, ArmyType.INFANTRY);
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        for (TerritoryType territoryToCheck : territories) {
            if (territoryToCheck != territoryOnCard) {
                assertFalse(card.matchesTerritory(territoryToCheck));
            }
        }
    }
}
