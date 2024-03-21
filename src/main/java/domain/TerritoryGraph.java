package domain;

import java.util.HashSet;
import java.util.Set;

public class TerritoryGraph {
    Set<TerritoryType> territories = new HashSet<>();
    public boolean addNewKey(TerritoryType territory) {
        if (territories.contains(territory)) {
            return false;
        }
        territories.add(territory);
        return true;
    }

    public boolean addNewAdjacency(TerritoryType startingTerritory, TerritoryType endingTerritory) {
        return territories.contains(startingTerritory) && territories.contains(endingTerritory);
    }
}
