package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PlayerTest {

    private static Stream<Arguments> territoryTypeGenerator() {
        return Stream.of(Arrays.stream(TerritoryType.values()).map(Arguments::of).toArray(Arguments[]::new));
    }

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test00_ownsTerritory_withNoneOwned_returnsFalse(TerritoryType territory) {
        Player player = new Player();
        assertFalse(player.ownsTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test01_ownsTerritory_withOneOwnedMatchesInput_returnsTrue(TerritoryType territory) {
        Player player = new Player();
        player.setTerritories(Set.of(territory));
        assertTrue(player.ownsTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test02_ownsTerritory_ownsAllButInput_returnsFalse(TerritoryType territory) {
        Player player = new Player();
        Set<TerritoryType> territories = new HashSet<>(Set.of(TerritoryType.values()));
        territories.remove(territory);
        player.setTerritories(territories);
        assertFalse(player.ownsTerritory(territory));
    }

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test03_ownsTerritory_ownsAll_returnsTrue(TerritoryType territory) {
        Player player = new Player();
        player.setTerritories(Set.of(TerritoryType.values()));
        assertTrue(player.ownsTerritory(territory));
    }

    private static Stream<Arguments> territoryTypeCombinationsGenerator() {
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
        return territoryArguments.stream();
    }

    @ParameterizedTest
    @MethodSource("territoryTypeCombinationsGenerator")
    public void test04_ownsTerritory_withOneMatchAndOneMismatch_returnsTrue(TerritoryType first, TerritoryType second) {
        Player player = new Player();
        player.setTerritories(Set.of(first, second));
        assertTrue(player.ownsTerritory(first));
    }

}
