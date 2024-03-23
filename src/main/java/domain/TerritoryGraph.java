package domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TerritoryGraph {
    private final Map<TerritoryType, Set<TerritoryType>> territories = new HashMap<>();
    private final Map<TerritoryType, Territory> territoryTypeToObject = new HashMap<>();

    public boolean addNewTerritory(Territory territory) {
        TerritoryType territoryType = territory.getTerritoryType();
        if (territoryTypeToObject.containsKey(territoryType)) {
            return false;
        }
        territoryTypeToObject.put(territoryType, territory);
        territories.put(territoryType, new HashSet<>());
        return true;
    }

    public boolean addNewAdjacency(TerritoryType startingTerritory, TerritoryType endingTerritory) {
        if (!(territories.containsKey(startingTerritory)
                && territories.containsKey(endingTerritory))) {
            return false;
        }
        if (territories.get(startingTerritory).contains(endingTerritory)
                && territories.get(endingTerritory).contains(startingTerritory)) {
            return false;
        }
        if (startingTerritory == endingTerritory) {
            return false;
        }
        territories.get(startingTerritory).add(endingTerritory);
        territories.get(endingTerritory).add(startingTerritory);
        return true;
    }

    public Territory getTerritory(TerritoryType territoryType) {
        return territoryTypeToObject.getOrDefault(territoryType, null);
    }

    public Set<Territory> findAdjacentTerritories(TerritoryType territoryType) {
        Set<TerritoryType> adjacent = territories.get(territoryType);
        Set<Territory> result = new HashSet<>();
        if (adjacent != null) {
            for (TerritoryType adjacentTerritoryType : adjacent) {
                result.add(territoryTypeToObject.get(adjacentTerritoryType));
            }
        }
        return result;
    }

    public boolean addSetOfAdjacencies(TerritoryType territoryType, Set<TerritoryType> adjacencies) {
        return false;
    }
}
