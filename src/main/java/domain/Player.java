package domain;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private Set<TerritoryType> territories = new HashSet<>();

    public Set<TerritoryType> getTerritories() {
        return null;
    }

    public boolean ownsTerritory(TerritoryType territory) {
        return territories.contains(territory);
    }

    void setTerritories(Set<TerritoryType> territories) {
        this.territories = territories;
    }
}
