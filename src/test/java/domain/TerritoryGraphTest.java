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
        TerritoryGraph territoryGraph = generateCompleteGraph();
        for (TerritoryType startingTerritory : Set.of(TerritoryType.values())) {
            for (TerritoryType endingTerritory : Set.of(TerritoryType.values())) {
                if (endingTerritory != startingTerritory) {
                    assertFalse(territoryGraph.addNewAdjacency(startingTerritory, endingTerritory));
                }
            }
        }
    }

    public TerritoryGraph generateCompleteGraph() {
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
        return territoryGraph;
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
    public void test13_getTerritory_withExistingMapEntry_doesNotMatch(
            TerritoryType existingTerritoryType) {
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

    @Test
    public void test14_getTerritory_withAllEntries() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType territoryType : Set.of(TerritoryType.values())) {
            Territory territory = EasyMock.createMock(Territory.class);
            EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
            EasyMock.replay(territory);
            territoryGraph.addNewTerritory(territory);
            assertEquals(territory, territoryGraph.getTerritory(territoryType));
            EasyMock.verify(territory);
        }
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test15_findAdjacentTerritories_withEmptyGraph(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        assertTrue(territoryGraph.findAdjacentTerritories(territoryType).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test16_findAdjacentTerritories_withOneTerritoryNoEdges(
            TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory territory = EasyMock.createMock(Territory.class);
        EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
        EasyMock.replay(territory);

        territoryGraph.addNewTerritory(territory);
        assertTrue(territoryGraph.findAdjacentTerritories(territoryType).isEmpty());
        EasyMock.verify(territory);
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test17_findAdjacentTerritories_withTwoVerticesOneEdge(
            TerritoryType firstTerritoryType, TerritoryType secondTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory firstTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(firstTerritory.getTerritoryType()).andReturn(firstTerritoryType);
        EasyMock.replay(firstTerritory);
        territoryGraph.addNewTerritory(firstTerritory);

        Territory secondTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(secondTerritory.getTerritoryType()).andReturn(secondTerritoryType);
        EasyMock.replay(secondTerritory);
        territoryGraph.addNewTerritory(secondTerritory);

        territoryGraph.addNewAdjacency(firstTerritoryType, secondTerritoryType);
        Set<Territory> adjacent = territoryGraph.findAdjacentTerritories(firstTerritoryType);
        assertEquals(1, adjacent.size());
        assertTrue(adjacent.contains(secondTerritory));

        adjacent = territoryGraph.findAdjacentTerritories(secondTerritoryType);
        assertEquals(1, adjacent.size());
        assertTrue(adjacent.contains(firstTerritory));
        EasyMock.verify(firstTerritory);
        EasyMock.verify(secondTerritory);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test18_findAdjacentTerritories_withAllVerticesNoEdges(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType territoryTypeToAdd : Set.of(TerritoryType.values())) {
            Territory territoryToAdd = EasyMock.createMock(Territory.class);
            EasyMock.expect(territoryToAdd.getTerritoryType()).andReturn(territoryTypeToAdd);
            EasyMock.replay(territoryToAdd);
            territoryGraph.addNewTerritory(territoryToAdd);
            EasyMock.verify(territoryToAdd);
        }

        Set<Territory> adjacent = territoryGraph.findAdjacentTerritories(territoryType);
        assertTrue(adjacent.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test19_findAdjacentTerritories_withCompleteGraph(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = generateCompleteGraph();
        Set<Territory> adjacent = territoryGraph.findAdjacentTerritories(territoryType);
        assertEquals(TerritoryType.values().length - 1, adjacent.size());
        for (TerritoryType adjacentTerritoryType : TerritoryType.values()) {
            if (adjacentTerritoryType != territoryType) {
                Territory adjacentTerritory = territoryGraph.getTerritory(adjacentTerritoryType);
                assertTrue(adjacent.contains(adjacentTerritory));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test20_addSetOfAdjacencies_withEmptyGraph_emptyAdjSet(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Set<TerritoryType> adjacencies = new HashSet<>();
        assertFalse(territoryGraph.addSetOfAdjacencies(territoryType, adjacencies));
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test21_addSetOfAdjacencies_withEmptyGraph_addSameType(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Set<TerritoryType> adjacencies = new HashSet<>();
        adjacencies.add(territoryType);
        assertFalse(territoryGraph.addSetOfAdjacencies(territoryType, adjacencies));
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test22_addSetOfAdjacencies_withEmptyGraph_addRemainingTypes(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Set<TerritoryType> adjacencies = new HashSet<>(Set.of(TerritoryType.values()));
        adjacencies.remove(territoryType);
        assertFalse(territoryGraph.addSetOfAdjacencies(territoryType, adjacencies));
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test23_addSetOfAdjacencies_withOneTerritory_emptyAdjSet(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory territory = EasyMock.createMock(Territory.class);
        EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
        EasyMock.replay(territory);

        Set<TerritoryType> adjacencies = new HashSet<>();
        territoryGraph.addNewTerritory(territory);
        assertFalse(territoryGraph.addSetOfAdjacencies(territoryType, adjacencies));
        EasyMock.verify(territory);
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test24_addSetOfAdjacencies_withDifferentTerritories_emptyAdjSet(TerritoryType firstTerritoryType, TerritoryType secondTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory territory = EasyMock.createMock(Territory.class);
        EasyMock.expect(territory.getTerritoryType()).andReturn(firstTerritoryType);
        EasyMock.replay(territory);

        Set<TerritoryType> adjacencies = new HashSet<>();
        territoryGraph.addNewTerritory(territory);
        assertFalse(territoryGraph.addSetOfAdjacencies(secondTerritoryType, adjacencies));
        EasyMock.verify(territory);
    }

    @ParameterizedTest
    @MethodSource("territoryGenerator")
    public void test25_addSetOfAdjacencies_withOneTerritory_addRemainingTypes(TerritoryType territoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory territory = EasyMock.createMock(Territory.class);
        EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
        EasyMock.replay(territory);

        Set<TerritoryType> adjacencies = new HashSet<>(Set.of(TerritoryType.values()));
        adjacencies.remove(territoryType);
        territoryGraph.addNewTerritory(territory);
        assertFalse(territoryGraph.addSetOfAdjacencies(territoryType, adjacencies));
        EasyMock.verify(territory);
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test26_addSetOfAdjacencies_withDifferentTerritories_setWithTerritoryFromGraph(TerritoryType firstTerritoryType, TerritoryType secondTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory territory = EasyMock.createMock(Territory.class);
        EasyMock.expect(territory.getTerritoryType()).andReturn(firstTerritoryType);
        EasyMock.replay(territory);

        Set<TerritoryType> adjacencies = new HashSet<>();
        adjacencies.add(firstTerritoryType);
        territoryGraph.addNewTerritory(territory);
        assertFalse(territoryGraph.addSetOfAdjacencies(secondTerritoryType, adjacencies));
        EasyMock.verify(territory);
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test27_addSetOfAdjacencies_withTwoVerticesNoEdges(TerritoryType firstTerritoryType, TerritoryType secondTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory firstTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(firstTerritory.getTerritoryType()).andReturn(firstTerritoryType);
        EasyMock.replay(firstTerritory);

        Territory secondTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(secondTerritory.getTerritoryType()).andReturn(secondTerritoryType);
        EasyMock.replay(secondTerritory);

        territoryGraph.addNewTerritory(firstTerritory);
        territoryGraph.addNewTerritory(secondTerritory);
        Set<TerritoryType> adjacencies = new HashSet<>();
        adjacencies.add(secondTerritoryType);
        assertTrue(territoryGraph.addSetOfAdjacencies(firstTerritoryType, adjacencies));

        Set<Territory> actualAdjacencies = territoryGraph.findAdjacentTerritories(firstTerritoryType);
        assertEquals(1, actualAdjacencies.size());
        assertTrue(actualAdjacencies.contains(secondTerritory));

        actualAdjacencies = territoryGraph.findAdjacentTerritories(secondTerritoryType);
        assertEquals(1, actualAdjacencies.size());
        assertTrue(actualAdjacencies.contains(firstTerritory));

        EasyMock.verify(firstTerritory);
        EasyMock.verify(secondTerritory);
    }

    @ParameterizedTest
    @MethodSource("territoryCombinationGenerator")
    public void test28_addSetOfAdjacencies_withTwoVerticesAndEdge(TerritoryType firstTerritoryType, TerritoryType secondTerritoryType) {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        Territory firstTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(firstTerritory.getTerritoryType()).andReturn(firstTerritoryType);
        EasyMock.replay(firstTerritory);

        Territory secondTerritory = EasyMock.createMock(Territory.class);
        EasyMock.expect(secondTerritory.getTerritoryType()).andReturn(secondTerritoryType);
        EasyMock.replay(secondTerritory);

        territoryGraph.addNewTerritory(firstTerritory);
        territoryGraph.addNewTerritory(secondTerritory);
        territoryGraph.addNewAdjacency(firstTerritoryType, secondTerritoryType);

        Set<TerritoryType> adjacencies = new HashSet<>();
        adjacencies.add(secondTerritoryType);
        assertFalse(territoryGraph.addSetOfAdjacencies(firstTerritoryType, adjacencies));

        EasyMock.verify(firstTerritory);
        EasyMock.verify(secondTerritory);
    }

    @Test
    public void test29_addSetOfAdjacencies_withAllVerticesNoEdges_sameInSet() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType territoryType : Set.of(TerritoryType.values())) {
            Territory territory = EasyMock.createMock(Territory.class);
            EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
            EasyMock.replay(territory);
            territoryGraph.addNewTerritory(territory);
            EasyMock.verify(territory);
        }

        Set<TerritoryType> adjacencies = new HashSet<>();
        adjacencies.add(TerritoryType.ALASKA);
        assertFalse(territoryGraph.addSetOfAdjacencies(TerritoryType.ALASKA, adjacencies));
    }

    @Test
    public void test30_addSetOfAdjacencies_withAllVerticesNoEdges_oneInSet() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType territoryType : Set.of(TerritoryType.values())) {
            Territory territory = EasyMock.createMock(Territory.class);
            EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
            EasyMock.replay(territory);
            territoryGraph.addNewTerritory(territory);
            EasyMock.verify(territory);
        }

        Set<TerritoryType> adjacencies = new HashSet<>();
        adjacencies.add(TerritoryType.ALASKA);
        assertTrue(territoryGraph.addSetOfAdjacencies(TerritoryType.ARGENTINA, adjacencies));
    }

    @Test
    public void test31_addSetOfAdjacencies_withAllVerticesNoEdges_twoInSet() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType territoryType : Set.of(TerritoryType.values())) {
            Territory territory = EasyMock.createMock(Territory.class);
            EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
            EasyMock.replay(territory);
            territoryGraph.addNewTerritory(territory);
            EasyMock.verify(territory);
        }

        Set<TerritoryType> adjacencies = new HashSet<>();
        adjacencies.add(TerritoryType.ALASKA);
        adjacencies.add(TerritoryType.ARGENTINA);
        assertTrue(territoryGraph.addSetOfAdjacencies(TerritoryType.AFGHANISTAN, adjacencies));

        Set<Territory> actualAdjacencies = territoryGraph.findAdjacentTerritories(TerritoryType.AFGHANISTAN);
        assertEquals(2, actualAdjacencies.size());
    }

    @Test
    public void test32_addSetOfAdjacencies_withAllVerticesNoEdges_remainingInSet() {
        TerritoryGraph territoryGraph = new TerritoryGraph();
        for (TerritoryType territoryType : Set.of(TerritoryType.values())) {
            Territory territory = EasyMock.createMock(Territory.class);
            EasyMock.expect(territory.getTerritoryType()).andReturn(territoryType);
            EasyMock.replay(territory);
            territoryGraph.addNewTerritory(territory);
            EasyMock.verify(territory);
        }

        Set<TerritoryType> adjacencies = new HashSet<>(Set.of(TerritoryType.values()));
        adjacencies.remove(TerritoryType.ALASKA);
        assertTrue(territoryGraph.addSetOfAdjacencies(TerritoryType.ALASKA, adjacencies));

        Set<Territory> actualAdjacencies = territoryGraph.findAdjacentTerritories(TerritoryType.ALASKA);
        assertEquals(41, actualAdjacencies.size());
    }
}
