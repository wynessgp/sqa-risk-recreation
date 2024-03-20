package domain;

public class Territory {
    private TerritoryType type;
    private Player playerInControl;
    private int numArmiesPresent;

    public Territory(TerritoryType type) {
        this.type = type;
        this.playerInControl = null;
        this.numArmiesPresent = 0;
    }
    public TerritoryType getType() {
        return type;
    }

    public boolean setPlayerInControl(Player player) {
        if (player == null) return false;
        this.playerInControl = player;
        return true;
    }

    public boolean setNumArmiesPresent(int newAmount) {
        if (newAmount < 0) return false;
        this.numArmiesPresent = newAmount;
        return true;
    }

    public Player getPlayerInControl() {
        return playerInControl;
    }

    public int getNumArmiesPresent() {
        return numArmiesPresent;
    }
}