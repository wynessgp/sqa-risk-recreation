package presentation;

import domain.TerritoryType;
import domain.WorldDominationGameEngine;

public class AttackLogic {
    private TerritoryType sourceTerritory;

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
        return !gameEngine.checkIfPlayerOwnsTerritory(territory, gameEngine.getCurrentPlayer());
    }

    boolean territoriesAreAdjacent(WorldDominationGameEngine gameEngine) {
        return false;
    }

}
