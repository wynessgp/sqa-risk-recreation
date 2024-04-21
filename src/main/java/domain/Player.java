package domain;

import java.util.Set;

public class Player {
    private Set<TerritoryType> territories;

    public Set<TerritoryType> getTerritories() {
        return null;
    }

    public boolean ownsTerritory(TerritoryType territory) {
        return territories != null;
    }

    void setTerritories(Set<TerritoryType> territories) {
        this.territories = territories;
    }
}
