package presentation;

import domain.PlayerColor;
import domain.TerritoryType;
import domain.WorldDominationGameEngine;

public class AttackLogic {
    private final WorldDominationGameEngine gameEngine;
    private TerritoryType sourceTerritory;
    private TerritoryType targetTerritory;
    private int attackArmies = 0;
    private int defendArmies = 0;
    private PlayerColor targetOwner;

    AttackLogic(WorldDominationGameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    AttackResult performAttack() {
        try {
            gameEngine.attackTerritory(sourceTerritory, targetTerritory, attackArmies, defendArmies);
            return AttackResult.SUCCESS;
        } catch (Exception e) {
            return AttackResult.parseError(e.getMessage());
        }
    }

    boolean sourceSelected() {
        return sourceTerritory != null;
    }

    boolean sourceArmiesSelected() {
        return attackArmies > 0;
    }

    boolean setSourceTerritory(TerritoryType territory) {
        if (gameEngine.checkIfPlayerOwnsTerritory(territory, gameEngine.getCurrentPlayer())) {
            sourceTerritory = territory;
            return true;
        }
        return false;
    }

    boolean setTargetTerritory(TerritoryType territory) {
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
        targetTerritory = null;
        attackArmies = 0;
        defendArmies = 0;
        targetOwner = null;
    }

    PlayerColor getTargetOwner() {
        if (targetOwner == null) {
            for (PlayerColor color : gameEngine.getPlayerOrder()) {
                if (gameEngine.checkIfPlayerOwnsTerritory(targetTerritory, color)) {
                    targetOwner = color;
                }
            }
        }
        return targetOwner;
    }

}
