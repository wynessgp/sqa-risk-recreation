package domain;

import java.util.Set;

public class Player {
    private final PlayerColor playerColor;
    private int numArmiesToPlace;

    public Set<TerritoryType> getTerritories() {
        return null;
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
