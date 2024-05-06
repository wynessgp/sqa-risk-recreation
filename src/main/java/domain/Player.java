package domain;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private final PlayerColor playerColor;
    private int numArmiesToPlace;
    private Set<TerritoryType> territories = new HashSet<>();
    private Set<Card> ownedCards = new HashSet<>();

    public Player(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public boolean ownsTerritory(TerritoryType territory) {
        return territories.contains(territory);
    }

    public void setNumArmiesToPlace(int newAmount) {
        numArmiesToPlace = newAmount;
    }

    public int getNumArmiesToPlace() {
        return numArmiesToPlace;
    }

    public PlayerColor getColor() {
        return playerColor;
    }

    public void addTerritoryToCollection(TerritoryType relevantTerritory) {
        this.territories.add(relevantTerritory);
    }

    void setTerritories(Set<TerritoryType> territories) {
        this.territories = territories;
    }

    Set<TerritoryType> getTerritories() {
        return territories;
    }

    Player() {
        playerColor = PlayerColor.SETUP;
    }

    public void setOwnedCards(Set<Card> cardsPlayerOwns) {
        this.ownedCards = new HashSet<>(cardsPlayerOwns);
    }

    public boolean ownsAllGivenCards(Set<Card> givenCards) {
        return ownedCards.containsAll(givenCards);
    }
}
