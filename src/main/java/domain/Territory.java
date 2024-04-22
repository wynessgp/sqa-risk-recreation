package domain;

public class Territory {
    private final TerritoryType territoryType;
    private int numArmiesPresent;
    private PlayerColor playerInControl;

    public Territory(TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.playerInControl = PlayerColor.SETUP;
        this.numArmiesPresent = 0;
    }

    Territory(PlayerColor playerToBeInControl, TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.playerInControl = playerToBeInControl;
        this.numArmiesPresent = 0;
    }

    public boolean setPlayerInControl(PlayerColor player) {
        if (player == PlayerColor.SETUP) {
            throw new IllegalArgumentException("Cannot set the player in control to setup");
        }
        if (player == playerInControl) {
            throw new IllegalArgumentException("Territory is already controlled by that player");
        }
        this.playerInControl = player;
        return true;
    }

    public boolean setNumArmiesPresent(int newAmount) {
        if (newAmount < 1) {
            throw new IllegalArgumentException("Number of armies to set should be greater than 0");
        }
        this.numArmiesPresent = newAmount;
        return true;
    }

    public int getNumArmiesPresent() {
        return numArmiesPresent;
    }

    public TerritoryType getTerritoryType() {
        return territoryType;
    }

    public boolean isOwnedByPlayer(PlayerColor playerToCheck) {
        return playerToCheck == playerInControl;
    }

}