package presentation;

import domain.TerritoryType;
import domain.WorldDominationGameEngine;

public class FortifyLogic {
    private final WorldDominationGameEngine gameEngine;
    private TerritoryType sourceTerritory;
    private TerritoryType destinationTerritory;
    int armiesToTransfer = 0;

    FortifyLogic(WorldDominationGameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    void setSourceTerritory(TerritoryType territory) {
        sourceTerritory = territory;
    }

    void setDestinationTerritory(TerritoryType territory) {
        destinationTerritory = territory;
    }

    void setArmiesToTransfer(int armies) {
        armiesToTransfer = armies;
    }

    void performFortify() {
        // TODO
    }

    boolean sourceSelected() {
        return sourceTerritory != null;
    }

    void reset() {
        sourceTerritory = null;
        destinationTerritory = null;
        armiesToTransfer = 0;
    }
}
