package domain;

public class TerritoryCard {
    private final TerritoryType territory;

    public TerritoryCard(TerritoryType territory, ArmyType army) {
        this.territory = territory;
    }

    public boolean matchesTerritory(TerritoryType territory) {
        return this.territory == territory;
    }
}
