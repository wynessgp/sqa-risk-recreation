package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ContinentTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void test00_matchesContinentTerritories_inputTooSmall_returnsFalse(int setSize) {
        Continent unitUnderTest = new Continent();
        Set<TerritoryType> setToPassIn = new HashSet<>();
        List<TerritoryType> allTerritories = List.of(TerritoryType.values());

        for (int i = 0; i < setSize; i++) {
            setToPassIn.add(allTerritories.get(i));
        }

        assertFalse(unitUnderTest.matchesContinentTerritories(setToPassIn));
    }
}
