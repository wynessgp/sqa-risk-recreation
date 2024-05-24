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
    private boolean sourceSelected = false;
    private boolean attackArmiesSet = false;
    private boolean ownerSelected = false;
    private boolean attackComplete = false;

    AttackLogic(WorldDominationGameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    AttackResult performAttack() {
        try {
            gameEngine.attackTerritory(sourceTerritory, targetTerritory, attackArmies, defendArmies);
            attackComplete = true;
            return AttackResult.SUCCESS;
        } catch (Exception e) {
            return AttackResult.parseError(e.getMessage());
        }
    }

    boolean isSourceSelected() {
        return sourceSelected;
    }

    boolean sourceArmiesSelected() {
        return attackArmiesSet;
    }

    boolean setSourceTerritory(TerritoryType territory) {
        attackComplete = false;
        if (gameEngine.checkIfPlayerOwnsTerritory(territory, gameEngine.getCurrentPlayer())) {
            sourceTerritory = territory;
            sourceSelected = true;
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
        attackArmiesSet = true;
    }

    void setDefendArmies(int armies) {
        this.defendArmies = armies;
    }

    void reset() {
        attackArmies = 0;
        defendArmies = 0;
        attackArmiesSet = false;
        sourceSelected = false;
        ownerSelected = false;
    }

    PlayerColor getTargetOwner() {
        if (!ownerSelected) {
            gameEngine.getPlayerOrder().stream().filter(color -> gameEngine
                    .checkIfPlayerOwnsTerritory(targetTerritory, color)).findFirst().ifPresent(color -> {
                        targetOwner = color;
                        ownerSelected = true;
                    });
        }
        return targetOwner;
    }

    boolean didDefenderLoseTerritory() {
        return !gameEngine.checkIfPlayerOwnsTerritory(targetTerritory, targetOwner);
    }

    TerritoryType getSourceTerritory() {
        return sourceTerritory;
    }

    TerritoryType getTargetTerritory() {
        return targetTerritory;
    }

    boolean isAttackComplete() {
        return attackComplete;
    }
}
