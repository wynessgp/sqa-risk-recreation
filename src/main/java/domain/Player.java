package domain;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private final PlayerColor playerColor;
    private int numArmiesToPlace;
    private final Set<TerritoryType> claimedTerritories = new HashSet<>();

    public Player(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public Set<TerritoryType> getTerritories() {
        return Set.copyOf(claimedTerritories);
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

    public void addTerritoryToCollection(TerritoryType relevantTerritory) {
        claimedTerritories.add(relevantTerritory);
    }
}
