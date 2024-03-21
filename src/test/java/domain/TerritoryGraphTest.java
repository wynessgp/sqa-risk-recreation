package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TerritoryGraphTest {
    private static Stream<Arguments> territoryGenerator() {
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        return territories.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test00_addNewKey_fromEmptySet_addTerritory(TerritoryType territory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        assertTrue(territoryGraph.addNewKey(territory));
    }

    @Test
    public void test01_addNewKey_withOneExisting_noDuplicate() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        TerritoryType territory = TerritoryType.ARGENTINA;
        territoryGraph.addNewKey(territory);
        territory = TerritoryType.ALASKA;
        assertTrue(territoryGraph.addNewKey(territory));
    }

    @Test
    public void test02_addNewKey_withTwoExisting_noDuplicate() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        TerritoryType territory = TerritoryType.BRAZIL;
        territoryGraph.addNewKey(territory);
        territory = TerritoryType.CONGO;
        territoryGraph.addNewKey(territory);
        territory = TerritoryType.ALASKA;
        assertTrue(territoryGraph.addNewKey(territory));
    }
}
