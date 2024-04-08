package domain;

public class Player {
    private final PlayerColor playerColor;
    private int numArmiesToPlace;

    public Player(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public void setNumArmiesToPlace(int newAmount) {
        numArmiesToPlace = newAmount;
    }

    public PlayerColor getColor() {
        return playerColor;
    }

    public int getNumArmiesToPlace() {
        return numArmiesToPlace;
    }
}
