package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ContinentTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void test00_matchesContinentTerritories_inputTooSmall_returnsFalse(int setSize) {
        Continent africa = Continent.AFRICA;
        Set<TerritoryType> setToPassIn = new HashSet<>();
        List<TerritoryType> allTerritories = List.of(TerritoryType.values());

        for (int i = 0; i < setSize; i++) {
            setToPassIn.add(allTerritories.get(i));
        }

        assertFalse(africa.matchesContinentTerritories(setToPassIn));
    }

    private static Stream<Arguments> generateAllContinentTerritorySetsAndAssociatedContinent() {
        // this method will generate tuples of:
        // (<necessary territories for continent>, ContinentType matching said continent)
        Set<Arguments> allContinentTerritories = new HashSet<>();

        Set<TerritoryType> africa = Set.of(TerritoryType.CONGO, TerritoryType.EAST_AFRICA, TerritoryType.EGYPT,
                TerritoryType.MADAGASCAR, TerritoryType.NORTH_AFRICA, TerritoryType.SOUTH_AFRICA);

        Set<TerritoryType> europe = Set.of(TerritoryType.EASTERN_EUROPE, TerritoryType.GREAT_BRITAIN,
                TerritoryType.ICELAND, TerritoryType.NORTHERN_EUROPE, TerritoryType.SCANDINAVIA,
                TerritoryType.SOUTHERN_EUROPE, TerritoryType.WESTERN_EUROPE);

        Set<TerritoryType> northAmerica = Set.of(TerritoryType.ALASKA, TerritoryType.ALBERTA,
                TerritoryType.CENTRAL_AMERICA, TerritoryType.EASTERN_EUROPE, TerritoryType.GREENLAND,
                TerritoryType.NORTHWEST_TERRITORY, TerritoryType.ONTARIO, TerritoryType.QUEBEC,
                TerritoryType.WESTERN_UNITED_STATES);

        Set<TerritoryType> southAmerica = Set.of(TerritoryType.ARGENTINA, TerritoryType.BRAZIL, TerritoryType.PERU,
                TerritoryType.VENEZUELA);

        Set<TerritoryType> oceania = Set.of(TerritoryType.EASTERN_AUSTRALIA, TerritoryType.INDONESIA,
                TerritoryType.NEW_GUINEA, TerritoryType.WESTERN_AUSTRALIA);

        Set<TerritoryType> asia = Set.of(TerritoryType.AFGHANISTAN, TerritoryType.CHINA, TerritoryType.INDIA,
                TerritoryType.IRKUTSK, TerritoryType.JAPAN, TerritoryType.KAMCHATKA, TerritoryType.MIDDLE_EAST,
                TerritoryType.MONGOLIA, TerritoryType.SIAM, TerritoryType.SIBERIA, TerritoryType.URAL,
                TerritoryType.YAKUTSK);

        allContinentTerritories.add(Arguments.of(africa, Continent.AFRICA));
        allContinentTerritories.add(Arguments.of(europe, Continent.EUROPE));
        allContinentTerritories.add(Arguments.of(northAmerica, Continent.NORTH_AMERICA));
        allContinentTerritories.add(Arguments.of(southAmerica, Continent.SOUTH_AMERICA));
        allContinentTerritories.add(Arguments.of(oceania, Continent.OCEANIA));
        allContinentTerritories.add(Arguments.of(asia, Continent.ASIA));

        return allContinentTerritories.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllContinentTerritorySetsAndAssociatedContinent")
    public void test01_matchesContinentTerritories_inputIsContinentSetMinusFirstTerritory_returnsFalse(
            Set<TerritoryType> validContinentTerritorySet, Continent continentToCheck) {
        // remove the first item of the valid set
        Iterator<TerritoryType> iterator = validContinentTerritorySet.iterator();
        TerritoryType toRemove = iterator.next();

        Set<TerritoryType> invalidContinentTerritorySet = new HashSet<>(validContinentTerritorySet);
        invalidContinentTerritorySet.remove(toRemove);

        assertFalse(continentToCheck.matchesContinentTerritories(invalidContinentTerritorySet));
    }
}
