package presentation;

import domain.TerritoryType;
import domain.WorldDominationGameEngine;

public class FortifyLogic {
    private final WorldDominationGameEngine gameEngine;
    private TerritoryType sourceTerritory;
    private TerritoryType destinationTerritory;
    private int armiesToTransfer = 0;
    private boolean sourceSelected = false;

    FortifyLogic(WorldDominationGameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    void setSourceTerritory(TerritoryType territory) {
        sourceTerritory = territory;
        sourceSelected = true;
    }

    void setDestinationTerritory(TerritoryType territory) {
        destinationTerritory = territory;
    }

    void setArmiesToTransfer(int armies) {
        armiesToTransfer = armies;
    }

    FortifyResult performFortify() {
        try {
            gameEngine.moveArmiesBetweenFriendlyTerritories(sourceTerritory, destinationTerritory, armiesToTransfer);
            return FortifyResult.SUCCESS;
        } catch (Exception e) {
            return FortifyResult.parseError(e.getMessage());
        }
    }

    boolean isSourceSelected() {
        return sourceSelected;
    }

    void reset() {
        sourceSelected = false;
        armiesToTransfer = 0;
    }
}
