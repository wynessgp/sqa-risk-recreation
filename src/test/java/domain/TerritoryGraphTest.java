package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test03_addNewKey_withAllExisting_addDuplicate_returnsFalse(TerritoryType territory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType territoryToAdd : Set.of(TerritoryType.values())) {
            territoryGraph.addNewKey(territoryToAdd);
        }
        assertFalse(territoryGraph.addNewKey(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test04_addNewAdjacency_withEmptyGraph_returnsFalse(TerritoryType startingTerritory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
            assertFalse(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
        }
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test05_addNewAdjacency_withOneVertex_returnsFalse(TerritoryType startingTerritory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        territoryGraph.addNewKey(startingTerritory);
        for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
            if (endingTerritory != startingTerritory) {
                assertFalse(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
            }
        }
    }

    private static Stream<Arguments> territoryCombinationGenerator() {
        Set<Arguments> territories = new HashSet<>();
        for (TerritoryType startingTerritory : Set.of(TerritoryType.values())) {
            for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
                if (endingTerritory != startingTerritory) {
                    territories.add(Arguments.of(startingTerritory, endingTerritory));
                }
            }
        }
        return Stream.of(territories.toArray(Arguments[]::new));
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test06_addNewAdjacency_withTwoVertices_noEdges(TerritoryType startingTerritory, TerritoryType endingTerritory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        territoryGraph.addNewKey(startingTerritory);
        territoryGraph.addNewKey(endingTerritory);
        assertTrue(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test07_addNewAdjacency_withTwoVertices_notInGraph(TerritoryType startingTerritory, TerritoryType endingTerritory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        territoryGraph.addNewKey(startingTerritory);
        territoryGraph.addNewKey(endingTerritory);
        for (TerritoryType territory : Set.of(TerritoryType.values())) {
            if (territory != startingTerritory && territory != endingTerritory) {
                assertFalse(territoryGraph.addNewAdjacency(startingTerritory, territory));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test07_addNewAdjacency_withTwoVertices_withEdge(TerritoryType startingTerritory, TerritoryType endingTerritory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        territoryGraph.addNewKey(startingTerritory);
        territoryGraph.addNewKey(endingTerritory);
        territoryGraph.addNewAdjacency(startingTerritory, endingTerritory);
        assertFalse(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
    }

    @Test
    public void test08_addNewAdjacency_withAllVertices_noEdges() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType startingTerritory : Set.of(TerritoryType.values())) {
            territoryGraph.addNewKey(startingTerritory);
            for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
                if (endingTerritory != startingTerritory) {
                    territoryGraph.addNewKey(endingTerritory);
                    assertTrue(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
                }
            }
        }
    }
}
