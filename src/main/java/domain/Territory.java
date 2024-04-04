package domain;

public class Territory {
    private final TerritoryType territoryType;
    private Player playerInControl;
    private int numArmiesPresent;


    public Territory(TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.playerInControl = new Player(PlayerColor.SETUP);
        this.numArmiesPresent = 0;
    }

    Territory(Player playerToBeInControl, TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.playerInControl = playerToBeInControl;
        this.numArmiesPresent = 0;
    }

    public boolean setPlayerInControl(Player player) {
        if (player.equals(playerInControl)) {
            return false;
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

    public Player getPlayerInControl() {
        return playerInControl;
    }

    public int getNumArmiesPresent() {
        return numArmiesPresent;
    }

    public TerritoryType getTerritoryType() {
        return territoryType;
    }
}