package presentation;

import domain.PlayerColor;
import domain.TerritoryType;
import domain.WorldDominationGameEngine;

public class AttackLogic {
    private TerritoryType sourceTerritory;
    private TerritoryType targetTerritory;
    private int attackArmies = 0;
    private int defendArmies = 0;

    void performAttack(WorldDominationGameEngine gameEngine) {
        gameEngine.attackTerritory(sourceTerritory, targetTerritory, attackArmies, defendArmies);
    }

    boolean sourceSelected() {
        return sourceTerritory != null;
    }

    boolean sourceArmiesSelected() {
        return attackArmies > 0;
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

    void setAttackArmies(int armies) {
        this.attackArmies = armies;
    }

    void setDefendArmies(int armies) {
        this.defendArmies = armies;
    }

    void reset() {
        sourceTerritory = null;
    }

    PlayerColor getTargetOwner(WorldDominationGameEngine gameEngine) {
        for (PlayerColor color : gameEngine.getPlayerOrder()) {
            if (gameEngine.checkIfPlayerOwnsTerritory(targetTerritory, color)) {
                return color;
            }
        }
        return null;
    }

}
