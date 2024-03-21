package domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TerritoryGraph {
    private final Set<TerritoryType> territories = new HashSet<>();
    private final Map<TerritoryType, TerritoryType> adjacencies = new HashMap<>();

    public boolean addNewKey(TerritoryType territory) {
        if (territories.contains(territory)) {
            return false;
        }
        territories.add(territory);
        return true;
    }

    public boolean addNewAdjacency(TerritoryType startingTerritory, TerritoryType endingTerritory) {
        if (!territories.contains(startingTerritory) || !territories.contains(endingTerritory)) {
            return false;
        }
        if (adjacencies.get(startingTerritory) == endingTerritory && adjacencies.get(endingTerritory) == startingTerritory) {
            return false;
        }
        adjacencies.put(startingTerritory, endingTerritory);
        adjacencies.put(endingTerritory, startingTerritory);
        return true;
    }
}
