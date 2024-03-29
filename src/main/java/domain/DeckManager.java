package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DeckManager {
    private final List<Card> deckOfCards;

    public DeckManager() {
        this.deckOfCards = new ArrayList<>();
    }

    protected DeckManager(List<Card> deckOfCards) {
        this.deckOfCards = deckOfCards;
    }

    public boolean initDeck() {
        if (!this.deckOfCards.isEmpty()) {
            throw new IllegalStateException("Cannot initialize a non-empty deck.");
        }
        generateTerritoryCards();
        this.deckOfCards.add(new WildCard());
        this.deckOfCards.add(new WildCard());
        return true;
    }

    private void generateTerritoryCards() {
        for (TerritoryType territory : TerritoryType.values()) {
            this.deckOfCards.add(new TerritoryCard(territory, PieceType.INFANTRY));
        }
    }

    public boolean shuffle() {
        if (this.deckOfCards.isEmpty()) {
            return false;
        }

        Collections.shuffle(this.deckOfCards);
        return true;
    }

    protected int getDeckSize() {
        return this.deckOfCards.size();
    }

}
