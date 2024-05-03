package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private static final Set<TerritoryType> AFRICA = Set.of(TerritoryType.CONGO, TerritoryType.EAST_AFRICA,
            TerritoryType.EGYPT, TerritoryType.MADAGASCAR, TerritoryType.NORTH_AFRICA, TerritoryType.SOUTH_AFRICA);

    private static final Set<TerritoryType> EUROPE = Set.of(TerritoryType.UKRAINE, TerritoryType.GREAT_BRITAIN,
            TerritoryType.ICELAND, TerritoryType.NORTHERN_EUROPE, TerritoryType.SCANDINAVIA,
            TerritoryType.SOUTHERN_EUROPE, TerritoryType.WESTERN_EUROPE);

    private static final Set<TerritoryType> NORTH_AMERICA = Set.of(TerritoryType.ALASKA, TerritoryType.ALBERTA,
            TerritoryType.CENTRAL_AMERICA, TerritoryType.UKRAINE, TerritoryType.GREENLAND,
            TerritoryType.NORTHWEST_TERRITORY, TerritoryType.ONTARIO, TerritoryType.QUEBEC,
            TerritoryType.WESTERN_UNITED_STATES);

    private static final Set<TerritoryType> SOUTH_AMERICA = Set.of(TerritoryType.ARGENTINA, TerritoryType.BRAZIL,
            TerritoryType.PERU, TerritoryType.VENEZUELA);

    private static final Set<TerritoryType> OCEANIA = Set.of(TerritoryType.EASTERN_AUSTRALIA, TerritoryType.INDONESIA,
            TerritoryType.NEW_GUINEA, TerritoryType.WESTERN_AUSTRALIA);

    private static final Set<TerritoryType> ASIA = Set.of(TerritoryType.AFGHANISTAN, TerritoryType.CHINA,
            TerritoryType.INDIA, TerritoryType.IRKUTSK, TerritoryType.JAPAN, TerritoryType.KAMCHATKA,
            TerritoryType.MIDDLE_EAST, TerritoryType.MONGOLIA, TerritoryType.SIAM, TerritoryType.SIBERIA,
            TerritoryType.URAL, TerritoryType.YAKUTSK);

    private static final int AFRICA_BONUS = 3;
    private static final int ASIA_BONUS = 7;
    private static final int EUROPE_BONUS = 5;
    private static final int NORTH_AMERICA_BONUS = 5;
    private static final int SOUTH_AMERICA_BONUS = 2;
    private static final int OCEANIA_BONUS = 2;

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

        allContinentTerritories.add(Arguments.of(AFRICA, Continent.AFRICA));
        allContinentTerritories.add(Arguments.of(EUROPE, Continent.EUROPE));
        allContinentTerritories.add(Arguments.of(NORTH_AMERICA, Continent.NORTH_AMERICA));
        allContinentTerritories.add(Arguments.of(SOUTH_AMERICA, Continent.SOUTH_AMERICA));
        allContinentTerritories.add(Arguments.of(OCEANIA, Continent.OCEANIA));
        allContinentTerritories.add(Arguments.of(ASIA, Continent.ASIA));

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

    @ParameterizedTest
    @MethodSource("generateAllContinentTerritorySetsAndAssociatedContinent")
    public void test02_matchesContinentTerritories_inputIsContinentSet_returnsTrue(
            Set<TerritoryType> validContinentTerritorySet, Continent continentToCheck) {
        assertTrue(continentToCheck.matchesContinentTerritories(validContinentTerritorySet));
    }

    private static Stream<Arguments> generateAllTwoContinentTerritorySetsAndAssociatedContinents() {
        Set<Arguments> toReturn = new HashSet<>();

        List<Set<TerritoryType>> territorySets = List.of(AFRICA, ASIA, EUROPE, NORTH_AMERICA, SOUTH_AMERICA, OCEANIA);
        List<Continent> associatedContinents = List.of(Continent.AFRICA, Continent.ASIA, Continent.EUROPE,
                Continent.NORTH_AMERICA, Continent.SOUTH_AMERICA, Continent.OCEANIA);

        for (int i = 0; i < associatedContinents.size(); i++) {
            for (int j = 0; j < associatedContinents.size(); j++) {
                if (i != j) {
                    Set<TerritoryType> combinedContinentTerritories = new HashSet<>(territorySets.get(i));
                    combinedContinentTerritories.addAll(territorySets.get(j));
                    toReturn.add(Arguments.of(combinedContinentTerritories, associatedContinents.get(i),
                            associatedContinents.get(j)));
                }
            }
        }
        return toReturn.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllTwoContinentTerritorySetsAndAssociatedContinents")
    public void test03_matchesContinentTerritories_inputContainsMultipleContinents_returnsTrueMultipleTimes(
            Set<TerritoryType> validContinentTerritorySet, Continent firstContinent, Continent secondContinent) {
        assertTrue(firstContinent.matchesContinentTerritories(validContinentTerritorySet));
        assertTrue(secondContinent.matchesContinentTerritories(validContinentTerritorySet));
    }

    private static Stream<Arguments> continentGenerator() {
        return Stream.of(
                Arguments.of(Continent.ASIA, "Asia"),
                Arguments.of(Continent.AFRICA, "Africa"),
                Arguments.of(Continent.EUROPE, "Europe"),
                Arguments.of(Continent.NORTH_AMERICA, "North America"),
                Arguments.of(Continent.SOUTH_AMERICA, "South America"),
                Arguments.of(Continent.OCEANIA, "Oceania")
        );
    }

    @ParameterizedTest
    @MethodSource("continentGenerator")
    public void test04_toString_matchesGivenString_expectTrue(Continent continent, String expectedName) {
        String continentName = continent.toString();
        assertEquals(expectedName, continentName);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void test05_getContinentBonusIfPlayerHasTerritories_inputMatchesNothing_returnsZero(int setSize) {
        Continent africa = Continent.AFRICA;
        Set<TerritoryType> setToPassIn = new HashSet<>();
        List<TerritoryType> allTerritories = List.of(TerritoryType.values());

        for (int i = 0; i < setSize; i++) {
            setToPassIn.add(allTerritories.get(i));
        }

        assertEquals(0, africa.getContinentBonusIfPlayerHasTerritories(setToPassIn));
    }

    private static Stream<Arguments> generateAllContinentTerritorySetsAndAssociatedContinentAndExpectedReturnValue() {
        // this method will generate tuples of:
        // (<necessary territories for continent>, ContinentType matching said continent, bonus with continent)
        Set<Arguments> allContinentTerritories = new HashSet<>();

        allContinentTerritories.add(Arguments.of(AFRICA, Continent.AFRICA, AFRICA_BONUS));
        allContinentTerritories.add(Arguments.of(EUROPE, Continent.EUROPE, EUROPE_BONUS));
        allContinentTerritories.add(Arguments.of(NORTH_AMERICA, Continent.NORTH_AMERICA, NORTH_AMERICA_BONUS));
        allContinentTerritories.add(Arguments.of(SOUTH_AMERICA, Continent.SOUTH_AMERICA, SOUTH_AMERICA_BONUS));
        allContinentTerritories.add(Arguments.of(OCEANIA, Continent.OCEANIA, OCEANIA_BONUS));
        allContinentTerritories.add(Arguments.of(ASIA, Continent.ASIA, ASIA_BONUS));

        return allContinentTerritories.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllContinentTerritorySetsAndAssociatedContinentAndExpectedReturnValue")
    public void test06_getContinentBonusIfPlayerHasTerritories_validInput_expectContinentGivenValue(
            Set<TerritoryType> validContinentTerritorySet, Continent continent, int expectedValue) {
        assertEquals(expectedValue, continent.getContinentBonusIfPlayerHasTerritories(validContinentTerritorySet));
    }

    private static Stream<Arguments> generateAllTwoContinentTerritorySetsAndAssociatedContinentsAndBonuses() {
        Set<Arguments> toReturn = new HashSet<>();

        List<Set<TerritoryType>> territorySets = List.of(AFRICA, ASIA, EUROPE, NORTH_AMERICA, SOUTH_AMERICA, OCEANIA);
        List<Continent> associatedContinents = List.of(Continent.AFRICA, Continent.ASIA, Continent.EUROPE,
                Continent.NORTH_AMERICA, Continent.SOUTH_AMERICA, Continent.OCEANIA);
        List<Integer> associatedContinentBonuses = List.of(AFRICA_BONUS, ASIA_BONUS, EUROPE_BONUS,
                NORTH_AMERICA_BONUS, SOUTH_AMERICA_BONUS, OCEANIA_BONUS);

        for (int i = 0; i < associatedContinents.size(); i++) {
            for (int j = 0; j < associatedContinents.size(); j++) {
                if (i != j) {
                    Set<TerritoryType> combinedContinentTerritories = new HashSet<>(territorySets.get(i));
                    combinedContinentTerritories.addAll(territorySets.get(j));
                    toReturn.add(Arguments.of(combinedContinentTerritories,
                            associatedContinents.get(i), associatedContinents.get(j),
                            associatedContinentBonuses.get(i), associatedContinentBonuses.get(j)));
                }
            }
        }
        return toReturn.stream();
    }

    @ParameterizedTest
    @MethodSource("generateAllTwoContinentTerritorySetsAndAssociatedContinentsAndBonuses")
    public void test07_getContinentBonusIfPlayerHasTerritories_twoContinentInput_expectCorrectBonusFromBoth(
            Set<TerritoryType> territorySet, Continent firstContinent, Continent secondContinent,
            int firstContinentBonus, int secondContinentBonus) {
        assertEquals(firstContinentBonus, firstContinent.getContinentBonusIfPlayerHasTerritories(territorySet));
        assertEquals(secondContinentBonus, secondContinent.getContinentBonusIfPlayerHasTerritories(territorySet));
    }


}
