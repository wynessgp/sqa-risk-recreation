package domain;

import java.util.Set;

public enum Continent {

    AFRICA(Set.of(TerritoryType.CONGO, TerritoryType.EAST_AFRICA, TerritoryType.EGYPT,
           TerritoryType.MADAGASCAR, TerritoryType.NORTH_AFRICA, TerritoryType.SOUTH_AFRICA), 3),

    ASIA(Set.of(TerritoryType.AFGHANISTAN, TerritoryType.CHINA, TerritoryType.INDIA,
            TerritoryType.IRKUTSK, TerritoryType.JAPAN, TerritoryType.KAMCHATKA, TerritoryType.MIDDLE_EAST,
            TerritoryType.MONGOLIA, TerritoryType.SIAM, TerritoryType.SIBERIA, TerritoryType.URAL,
            TerritoryType.YAKUTSK), 7),

    EUROPE(Set.of(TerritoryType.EASTERN_EUROPE, TerritoryType.GREAT_BRITAIN,
            TerritoryType.ICELAND, TerritoryType.NORTHERN_EUROPE, TerritoryType.SCANDINAVIA,
            TerritoryType.SOUTHERN_EUROPE, TerritoryType.WESTERN_EUROPE), 5),

    NORTH_AMERICA(Set.of(TerritoryType.ALASKA, TerritoryType.ALBERTA,
            TerritoryType.CENTRAL_AMERICA, TerritoryType.EASTERN_UNITED_STATES, TerritoryType.GREENLAND,
            TerritoryType.NORTHWEST_TERRITORY, TerritoryType.ONTARIO, TerritoryType.QUEBEC,
            TerritoryType.WESTERN_UNITED_STATES), 5),

    OCEANIA(Set.of(TerritoryType.EASTERN_AUSTRALIA, TerritoryType.INDONESIA,
            TerritoryType.NEW_GUINEA, TerritoryType.WESTERN_AUSTRALIA), 2),

    SOUTH_AMERICA(Set.of(TerritoryType.ARGENTINA, TerritoryType.BRAZIL, TerritoryType.PERU,
            TerritoryType.VENEZUELA), 2);

    private final Set<TerritoryType> associatedTerritories;
    private final int continentBonus;

    Continent(Set<TerritoryType> associatedTerritories, int continentArmyBonus) {
        this.associatedTerritories = associatedTerritories;
        this.continentBonus = continentArmyBonus;
    }

    boolean matchesContinentTerritories(Set<TerritoryType> playerTerritoriesInQuestion) {
        return playerTerritoriesInQuestion.containsAll(associatedTerritories);
    }

    public String toString() {
        String[] nameArray = this.name().split("_");
        StringBuilder name = new StringBuilder();
        for (String s : nameArray) {
            name.append(s.charAt(0)).append(s.substring(1).toLowerCase()).append(" ");
        }
        return name.deleteCharAt(name.length() - 1).toString();
    }

    public int getContinentBonusIfPlayerHasTerritories(Set<TerritoryType> setToPassIn) {
        return (matchesContinentTerritories(setToPassIn)) ? continentBonus : 0;
    }
}
