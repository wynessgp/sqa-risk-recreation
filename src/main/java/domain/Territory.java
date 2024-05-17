package domain;

class Territory {
    private final TerritoryType territoryType;
    private int numArmiesPresent;
    private PlayerColor playerInControl;

    Territory(TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.playerInControl = PlayerColor.SETUP;
        this.numArmiesPresent = 0;
    }

    Territory(PlayerColor playerToBeInControl, TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.playerInControl = playerToBeInControl;
        this.numArmiesPresent = 0;
    }

    boolean setPlayerInControl(PlayerColor player) {
        if (player == PlayerColor.SETUP) {
            throw new IllegalArgumentException("Cannot set the player in control to setup");
        }
        if (player == playerInControl) {
            throw new IllegalArgumentException("Territory is already controlled by that player");
        }
        this.playerInControl = player;
        return true;
    }

    boolean setNumArmiesPresent(int newAmount) {
        if (newAmount < 0) {
            throw new IllegalArgumentException("Number of armies to set should be >= 0");
        }
        this.numArmiesPresent = newAmount;
        return true;
    }

    int getNumArmiesPresent() {
        return numArmiesPresent;
    }

    TerritoryType getTerritoryType() {
        return territoryType;
    }

    boolean isOwnedByPlayer(PlayerColor playerToCheck) {
        return playerToCheck == playerInControl;
    }

}