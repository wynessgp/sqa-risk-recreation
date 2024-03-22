package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TerritoryGraphTest {
    private static Set<Arguments> territoryCombinations;

    private static Stream<Arguments> territoryGenerator() {
        Set<TerritoryType> territories = Set.of(TerritoryType.values());
        return territories.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test00_addNewTerritory_fromEmptySet_addTerritory(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory territory = EasyMock.createMock(Territory.class);

        EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
        EasyMock.replay(territory);

        assertTrue(territoryGraph.addNewTerritory(territory));
        EasyMock.verify(territory);
    }

    @Test
    public void test01_addNewTerritory_withOneExisting_noDuplicate() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory firstTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(firstTerritory.getTerritoryType()).andReturn(TerritoryType.ARGENTINA);
        EasyMock.replay(firstTerritory);
        territoryGraph.addNewTerritory(firstTerritory);

        Territory secondTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(secondTerritory.getTerritoryType()).andReturn(TerritoryType.ALASKA);
        EasyMock.replay(secondTerritory);

        assertTrue(territoryGraph.addNewTerritory(secondTerritory));
        EasyMock.verify(firstTerritory);
        EasyMock.verify(secondTerritory);
    }

    @Test
    public void test02_addNewTerritory_withTwoExisting_noDuplicate() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory firstTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(firstTerritory.getTerritoryType()).andReturn(TerritoryType.BRAZIL);
        EasyMock.replay(firstTerritory);
        territoryGraph.addNewTerritory(firstTerritory);

        Territory secondTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(secondTerritory.getTerritoryType()).andReturn(TerritoryType.CONGO);
        EasyMock.replay(secondTerritory);
        territoryGraph.addNewTerritory(secondTerritory);

        Territory thirdTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(thirdTerritory.getTerritoryType()).andReturn(TerritoryType.ALASKA);
        EasyMock.replay(thirdTerritory);

        assertTrue(territoryGraph.addNewTerritory(thirdTerritory));
        EasyMock.verify(firstTerritory);
        EasyMock.verify(secondTerritory);
        EasyMock.verify(thirdTerritory);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test03_addNewTerritory_withAllExisting_addDuplicate_returnsFalse(
            TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType territoryTypeToAdd : Set.of(TerritoryType.values())) {
            Territory territory = EasyMock.createMock(Territory.class);
            EasyMock.expect(territory.getTerritoryType()).andReturn(territoryTypeToAdd);
            EasyMock.replay(territory);
            territoryGraph.addNewTerritory(territory);
            EasyMock.verify(territory);
        }

        Territory territory = EasyMock.createMock(Territory.class);
        EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
        EasyMock.replay(territory);

        assertFalse(territoryGraph.addNewTerritory(territory));
        EasyMock.verify(territory);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test04_addNewAdjacency_withEmptyGraph_returnsFalse(
            TerritoryType startingTerritory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
            assertFalse(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
        }
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test05_addNewAdjacency_withOneVertex_returnsFalse(TerritoryType startingTerritory) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory territory = EasyMock.createMock(Territory.class);

        EasyMock.expect(territory.getTerritoryType()).andReturn(startingTerritory);
        EasyMock.replay(territory);
        territoryGraph.addNewTerritory(territory);

        for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
            if (endingTerritory != startingTerritory) {
                assertFalse(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
            }
        }
        EasyMock.verify(territory);
    }

    private static Stream<Arguments> territoryCombinationGenerator() {
        if (territoryCombinations == null) {
            performTerritoryCombinationGeneration();
        }
        return territoryCombinations.stream();
    }

    private static void performTerritoryCombinationGeneration() {
        Set<Set<TerritoryType>> territoriesNoDuplicates = new HashSet<>();
        for (TerritoryType startingTerritory : TerritoryType.values()) {
            for (TerritoryType endingTerritory : TerritoryType.values()) {
                if (endingTerritory != startingTerritory) {
                    Set<TerritoryType> territoryPair = new HashSet<>();
                    territoryPair.add(startingTerritory);
                    territoryPair.add(endingTerritory);
                    territoriesNoDuplicates.add(territoryPair);
                }
            }
        }
        Set<Arguments> territoryArguments = new HashSet<>();
        for (Set<TerritoryType> territoryPair : territoriesNoDuplicates) {
            Iterator<TerritoryType> iterator = territoryPair.iterator();
            TerritoryType startingTerritoryType = iterator.next();
            TerritoryType endingTerritoryType = iterator.next();
            territoryArguments.add(Arguments.of(startingTerritoryType, endingTerritoryType));
        }
        territoryCombinations = territoryArguments;
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test06_addNewAdjacency_withTwoVertices_noEdges(
            TerritoryType startingTerritoryType, TerritoryType endingTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();

        Territory startingTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(startingTerritory.getTerritoryType()).andReturn(startingTerritoryType);
        EasyMock.replay(startingTerritory);
        territoryGraph.addNewTerritory(startingTerritory);

        Territory endingTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(endingTerritory.getTerritoryType()).andReturn(endingTerritoryType);
        EasyMock.replay(endingTerritory);
        territoryGraph.addNewTerritory(endingTerritory);

        assertTrue(territoryGraph.addNewAdjacency(startingTerritoryType, endingTerritoryType));
        EasyMock.verify(startingTerritory);
        EasyMock.verify(endingTerritory);
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test07_addNewAdjacency_withTwoVertices_notInGraph(
            TerritoryType startingTerritoryType, TerritoryType endingTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();

        Territory startingTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(startingTerritory.getTerritoryType()).andReturn(startingTerritoryType);
        EasyMock.replay(startingTerritory);
        territoryGraph.addNewTerritory(startingTerritory);

        Territory endingTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(endingTerritory.getTerritoryType()).andReturn(endingTerritoryType);
        EasyMock.replay(endingTerritory);
        territoryGraph.addNewTerritory(endingTerritory);

        for (TerritoryType territoryType : Set.of(TerritoryType.values())) {
            if (territoryType != startingTerritoryType && territoryType != endingTerritoryType) {
                assertFalse(territoryGraph.addNewAdjacency(startingTerritoryType, territoryType));
            }
        }
        EasyMock.verify(startingTerritory);
        EasyMock.verify(endingTerritory);
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test07_addNewAdjacency_withTwoVertices_withEdge(
            TerritoryType startingTerritoryType, TerritoryType endingTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();

        Territory startingTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(startingTerritory.getTerritoryType()).andReturn(startingTerritoryType);
        EasyMock.replay(startingTerritory);
        territoryGraph.addNewTerritory(startingTerritory);

        Territory endingTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(endingTerritory.getTerritoryType()).andReturn(endingTerritoryType);
        EasyMock.replay(endingTerritory);
        territoryGraph.addNewTerritory(endingTerritory);

        territoryGraph.addNewAdjacency(startingTerritoryType, endingTerritoryType);
        assertFalse(territoryGraph.addNewAdjacency(startingTerritoryType, endingTerritoryType));
        EasyMock.verify(startingTerritory);
        EasyMock.verify(endingTerritory);
    }

    @Test
    public void test08_addNewAdjacency_withAllVertices_noEdges() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Set<Set<TerritoryType>> territoriesNoDuplicates = new HashSet<>();
        for (TerritoryType startingTerritory : Set.of(TerritoryType.values())) {
            for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
                if (endingTerritory != startingTerritory) {
                    Set<TerritoryType> territoryPair = new HashSet<>();
                    territoryPair.add(startingTerritory);
                    territoryPair.add(endingTerritory);
                    territoriesNoDuplicates.add(territoryPair);
                }
            }
        }

        for (Set<TerritoryType> territoryPair : territoriesNoDuplicates) {
            Iterator<TerritoryType> iterator = territoryPair.iterator();
            TerritoryType startingTerritoryType = iterator.next();
            Territory startingTerritory = EasyMock.createMock(Territory.class);
            EasyMock.expect(startingTerritory.getTerritoryType()).andReturn(startingTerritoryType);
            EasyMock.replay(startingTerritory);
            territoryGraph.addNewTerritory(startingTerritory);

            TerritoryType endingTerritoryType = iterator.next();
            Territory endingTerritory = EasyMock.createMock(Territory.class);
            EasyMock.expect(endingTerritory.getTerritoryType()).andReturn(endingTerritoryType);
            EasyMock.replay(endingTerritory);
            territoryGraph.addNewTerritory(endingTerritory);

            assertTrue(territoryGraph.addNewAdjacency(startingTerritoryType, endingTerritoryType));
            EasyMock.verify(startingTerritory);
            EasyMock.verify(endingTerritory);
        }
    }

    @Test
    public void test09_addNewAdjacency_withCompleteGraph() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType startingTerritoryType : Set.of(TerritoryType.values())) {
            Territory startingTerritory = EasyMock.createMock(Territory.class);
            EasyMock.expect(startingTerritory.getTerritoryType()).andReturn(startingTerritoryType);
            EasyMock.replay(startingTerritory);
            territoryGraph.addNewTerritory(startingTerritory);
            EasyMock.verify(startingTerritory);

            for (TerritoryType endingTerritoryType : Set.of(TerritoryType.values())) {
                if (endingTerritoryType != startingTerritoryType) {
                    Territory endingTerritory = EasyMock.createMock(Territory.class);
                    EasyMock.expect(endingTerritory.getTerritoryType())
                            .andReturn(endingTerritoryType);
                    EasyMock.replay(endingTerritory);
                    territoryGraph.addNewTerritory(endingTerritory);

                    territoryGraph.addNewAdjacency(startingTerritoryType, endingTerritoryType);
                    EasyMock.verify(endingTerritory);
                }
            }
        }

        for (TerritoryType startingTerritory : Set.of(TerritoryType.values())) {
            for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
                if (endingTerritory != startingTerritory) {
                    assertFalse(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test10_addNewAdjacency_withOneVertex_duplicateValue(
            TerritoryType startingTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory startingTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(startingTerritory.getTerritoryType()).andReturn(startingTerritoryType);
        EasyMock.replay(startingTerritory);
        territoryGraph.addNewTerritory(startingTerritory);
        EasyMock.verify(startingTerritory);
        assertFalse(territoryGraph.addNewAdjacency(startingTerritoryType, startingTerritoryType));
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test11_getTerritory_withEmptyMap(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        assertNull(territoryGraph.getTerritory(territoryType));
    }

    @ParameterizedTest()
    @MethodSource("territoryGenerator")
    public void test12_getTerritory_withExistingMapEntry(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory territory = EasyMock.createMock(Territory.class);
        EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
        EasyMock.replay(territory);

        territoryGraph.addNewTerritory(territory);
        assertEquals(territory, territoryGraph.getTerritory(territoryType));
        EasyMock.verify(territory);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test13_getTerritory_withExistingMapEntry_doesNotMatch(TerritoryType existingTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory existingTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(existingTerritory.getTerritoryType()).andReturn(existingTerritoryType);
        EasyMock.replay(existingTerritory);

        territoryGraph.addNewTerritory(existingTerritory);
        for (TerritoryType territoryType : Set.of(TerritoryType.values())) {
            if (territoryType != existingTerritoryType) {
                assertNull(territoryGraph.getTerritory(territoryType));
            }
        }
        EasyMock.verify(existingTerritory);
    }
}
