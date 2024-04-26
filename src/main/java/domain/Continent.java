package domain;

import java.util.Set;

public enum Continent {
    AFRICA(Set.of(TerritoryType.CONGO, TerritoryType.EAST_AFRICA, TerritoryType.EGYPT,
            TerritoryType.MADAGASCAR, TerritoryType.NORTH_AFRICA, TerritoryType.SOUTH_AFRICA)),

    ASIA(Set.of(TerritoryType.AFGHANISTAN, TerritoryType.CHINA, TerritoryType.INDIA,
            TerritoryType.IRKUTSK, TerritoryType.JAPAN, TerritoryType.KAMCHATKA, TerritoryType.MIDDLE_EAST,
            TerritoryType.MONGOLIA, TerritoryType.SIAM, TerritoryType.SIBERIA, TerritoryType.URAL,
            TerritoryType.YAKUTSK)),

    EUROPE(Set.of(TerritoryType.EASTERN_EUROPE, TerritoryType.GREAT_BRITAIN,
            TerritoryType.ICELAND, TerritoryType.NORTHERN_EUROPE, TerritoryType.SCANDINAVIA,
            TerritoryType.SOUTHERN_EUROPE, TerritoryType.WESTERN_EUROPE)),

    NORTH_AMERICA(Set.of(TerritoryType.ALASKA, TerritoryType.ALBERTA,
            TerritoryType.CENTRAL_AMERICA, TerritoryType.EASTERN_EUROPE, TerritoryType.GREENLAND,
            TerritoryType.NORTHWEST_TERRITORY, TerritoryType.ONTARIO, TerritoryType.QUEBEC,
            TerritoryType.WESTERN_UNITED_STATES)),

    OCEANIA(Set.of(TerritoryType.EASTERN_AUSTRALIA, TerritoryType.INDONESIA,
            TerritoryType.NEW_GUINEA, TerritoryType.WESTERN_AUSTRALIA)),

    SOUTH_AMERICA(Set.of(TerritoryType.ARGENTINA, TerritoryType.BRAZIL, TerritoryType.PERU,
            TerritoryType.VENEZUELA));

    private final Set<TerritoryType> associatedTerritories;

    Continent(Set<TerritoryType> associatedTerritories) {
        this.associatedTerritories = associatedTerritories;
    }

    boolean matchesContinentTerritories(Set<TerritoryType> playerTerritoriesInQuestion) {
        return false;
    }
}
