package domain;

public class Territory {
    private final TerritoryType territoryType;
    private int numArmiesPresent;


    public Territory(TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.numArmiesPresent = 0;
    }

    Territory(PlayerColor playerToBeInControl, TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.numArmiesPresent = 0;
    }

    public void setPlayerInControl(PlayerColor player) {
        if (player == PlayerColor.SETUP) {
            throw new IllegalArgumentException("Cannot set the player in control to setup");
        }
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
}