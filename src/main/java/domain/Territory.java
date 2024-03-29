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


    public boolean setPlayerInControl(Player player) {
        if (player.equals(playerInControl)) {
            return false;
        }
        this.playerInControl = player;
        return true;
    }

    public boolean setNumArmiesPresent(int newAmount) {
        if (newAmount < 0) {
            return false;
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
