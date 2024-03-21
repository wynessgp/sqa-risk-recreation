package domain;

import java.util.*;

public class TerritoryGraph {
    private final Map<TerritoryType, Set<TerritoryType>> territories = new HashMap<>();

    public boolean addNewKey(TerritoryType territory) {
        if (territories.containsKey(territory)) {
            return false;
        }
        territories.put(territory, new HashSet<>());
        return true;
    }

    public boolean addNewAdjacency(TerritoryType startingTerritory, TerritoryType endingTerritory) {
        if (!(territories.containsKey(startingTerritory) && territories.containsKey(endingTerritory))) {
            return false;
        }
        if (territories.get(startingTerritory).contains(endingTerritory) && territories.get(endingTerritory).contains(startingTerritory)) {
            return false;
        }
        territories.get(startingTerritory).add(endingTerritory);
        territories.get(endingTerritory).add(startingTerritory);
        return true;
    }
}
