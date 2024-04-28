package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TerritoryTypeTest {

    private static Stream<Arguments> territoryTypeGenerator() {
        return Stream.of(
                Arguments.of(TerritoryType.ALASKA, "Alaska"),
                Arguments.of(TerritoryType.ALBERTA, "Alberta"),
                Arguments.of(TerritoryType.CENTRAL_AMERICA, "Central America"),
                Arguments.of(TerritoryType.EASTERN_UNITED_STATES, "Eastern United States"),
                Arguments.of(TerritoryType.GREENLAND, "Greenland"),
                Arguments.of(TerritoryType.NORTHWEST_TERRITORY, "Northwest Territory"),
                Arguments.of(TerritoryType.ONTARIO, "Ontario"),
                Arguments.of(TerritoryType.QUEBEC, "Quebec"),
                Arguments.of(TerritoryType.WESTERN_UNITED_STATES, "Western United States"),
                Arguments.of(TerritoryType.ARGENTINA, "Argentina"),
                Arguments.of(TerritoryType.BRAZIL, "Brazil"),
                Arguments.of(TerritoryType.PERU, "Peru"),
                Arguments.of(TerritoryType.VENEZUELA, "Venezuela"),
                Arguments.of(TerritoryType.UKRAINE, "Ukraine"),
                Arguments.of(TerritoryType.GREAT_BRITAIN, "Great Britain"),
                Arguments.of(TerritoryType.ICELAND, "Iceland"),
                Arguments.of(TerritoryType.NORTHERN_EUROPE, "Northern Europe"),
                Arguments.of(TerritoryType.SCANDINAVIA, "Scandinavia"),
                Arguments.of(TerritoryType.SOUTHERN_EUROPE, "Southern Europe"),
                Arguments.of(TerritoryType.WESTERN_EUROPE, "Western Europe"),
                Arguments.of(TerritoryType.CONGO, "Congo"),
                Arguments.of(TerritoryType.EAST_AFRICA, "East Africa"),
                Arguments.of(TerritoryType.EGYPT, "Egypt"),
                Arguments.of(TerritoryType.MADAGASCAR, "Madagascar"),
                Arguments.of(TerritoryType.NORTH_AFRICA, "North Africa"),
                Arguments.of(TerritoryType.SOUTH_AFRICA, "South Africa"),
                Arguments.of(TerritoryType.AFGHANISTAN, "Afghanistan"),
                Arguments.of(TerritoryType.CHINA, "China"),
                Arguments.of(TerritoryType.INDIA, "India"),
                Arguments.of(TerritoryType.IRKUTSK, "Irkutsk"),
                Arguments.of(TerritoryType.JAPAN, "Japan"),
                Arguments.of(TerritoryType.KAMCHATKA, "Kamchatka"),
                Arguments.of(TerritoryType.MIDDLE_EAST, "Middle East"),
                Arguments.of(TerritoryType.MONGOLIA, "Mongolia"),
                Arguments.of(TerritoryType.SIAM, "Siam"),
                Arguments.of(TerritoryType.SIBERIA, "Siberia"),
                Arguments.of(TerritoryType.URAL, "Ural"),
                Arguments.of(TerritoryType.YAKUTSK, "Yakutsk"),
                Arguments.of(TerritoryType.EASTERN_AUSTRALIA, "Eastern Australia"),
                Arguments.of(TerritoryType.INDONESIA, "Indonesia"),
                Arguments.of(TerritoryType.NEW_GUINEA, "New Guinea"),
                Arguments.of(TerritoryType.WESTERN_AUSTRALIA, "Western Australia")
        );
    }

    @ParameterizedTest
    @MethodSource("territoryTypeGenerator")
    public void test00_toString_withValidResult_returnsProperString(TerritoryType territory, String expected) {
        String result = territory.toString();
        assertEquals(expected, result);
    }

}
