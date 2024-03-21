package domain;

public class Territory {
    private TerritoryType territoryType;;
    private Player playerInControl;
    private int numArmiesPresent;
    private Continent continent;

    public Territory(TerritoryType territoryType) {
        this.territoryType = territoryType;
        this.playerInControl = null;
        this.numArmiesPresent = 0;
    }


    public boolean setPlayerInControl(Player player) {
        if (player == null || player.equals(playerInControl)) return false;
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

    public TerritoryType getTerritoryType() {
        return territoryType;
    }
}