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
        if (!isValidAdjacency(startingTerritory, endingTerritory)) {
            return false;
        }
        territories.get(startingTerritory).add(endingTerritory);
        territories.get(endingTerritory).add(startingTerritory);
        return true;
    }

    private boolean isValidAdjacency(TerritoryType startingTerritory, TerritoryType endingTerritory) {
        return territories.containsKey(startingTerritory) && territories.containsKey(endingTerritory)
                && !territories.get(startingTerritory).contains(endingTerritory)
                && (startingTerritory != endingTerritory);
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
        Set<TerritoryType> currentAdjacencies = territories.get(territoryType);
        if (graphContainsAdjacencies(territoryType, adjacencies, currentAdjacencies)) {
            return false;
        }
        addAdjaenciesToGraph(territoryType, adjacencies, currentAdjacencies);
        return true;
    }

    private boolean graphContainsAdjacencies(TerritoryType territoryType, Set<TerritoryType> adjacencies,
                                             Set<TerritoryType> currentAdjacencies) {
        return (!territoryTypeToObject.containsKey(territoryType) || adjacencies.isEmpty()
                || adjacencies.contains(territoryType)) || graphHasAdjacencyMapping(adjacencies, currentAdjacencies);
    }

    private boolean graphHasAdjacencyMapping(Set<TerritoryType> adjacencies, Set<TerritoryType> currentAdjacencies) {
        for (TerritoryType adjacentTerritoryType : adjacencies) {
            if (!territoryTypeToObject.containsKey(adjacentTerritoryType)
                    || currentAdjacencies.contains(adjacentTerritoryType)) {
                return true;
            }
        }
        return false;
    }

    private void addAdjaenciesToGraph(TerritoryType territoryType, Set<TerritoryType> adjacencies,
                                      Set<TerritoryType> currentAdjacencies) {
        currentAdjacencies.addAll(adjacencies);
        for (TerritoryType adjacentTerritoryType : adjacencies) {
            territories.get(adjacentTerritoryType).add(territoryType);
        }
    }
}
