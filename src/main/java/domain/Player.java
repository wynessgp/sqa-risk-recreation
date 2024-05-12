package domain;

import java.util.HashSet;
import java.util.Set;

class Player {
    private final PlayerColor playerColor;
    private int numArmiesToPlace;
    private Set<TerritoryType> territories = new HashSet<>();
    private Set<Card> ownedCards = new HashSet<>();

    Player(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    boolean ownsTerritory(TerritoryType territory) {
        return territories.contains(territory);
    }

    void setNumArmiesToPlace(int newAmount) {
        numArmiesToPlace = newAmount;
    }

    int getNumArmiesToPlace() {
        return numArmiesToPlace;
    }

    PlayerColor getColor() {
        return playerColor;
    }

    void addTerritoryToCollection(TerritoryType relevantTerritory) {
        this.territories.add(relevantTerritory);
    }

    boolean ownsAllGivenCards(Set<Card> givenCards) {
        return ownedCards.containsAll(givenCards);
    }

    void removeAllGivenCards(Set<Card> cardsToRemove) {
        ownedCards.removeAll(cardsToRemove);
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

    int getNumCardsHeld() {
        return this.ownedCards.size();
    }

    void setOwnedCards(Set<Card> cardsPlayerOwns) {
        this.ownedCards = new HashSet<>(cardsPlayerOwns);
    }

    Set<Card> getOwnedCards() {
        return new HashSet<>(ownedCards);
    }

    void addCardsToCollection(Set<Card> wildCardCollection) {

    }

}
