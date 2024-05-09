package presentation;

import domain.TerritoryType;
import domain.WorldDominationGameEngine;

public class AttackLogic {
    private TerritoryType sourceTerritory;
    private TerritoryType targetTerritory;

    boolean sourceSelected() {
        return sourceTerritory != null;
    }

    boolean setSourceTerritory(TerritoryType territory, WorldDominationGameEngine gameEngine) {
        if (gameEngine.checkIfPlayerOwnsTerritory(territory, gameEngine.getCurrentPlayer())) {
            sourceTerritory = territory;
            return true;
        }
        return false;
    }

    boolean setTargetTerritory(TerritoryType territory, WorldDominationGameEngine gameEngine) {
        if (gameEngine.checkIfPlayerOwnsTerritory(territory, gameEngine.getCurrentPlayer())) {
            return false;
        }
        targetTerritory = territory;
        return true;
    }

    boolean territoriesAreAdjacent(WorldDominationGameEngine gameEngine) {
        return false;
    }

}
